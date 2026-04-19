# Comparison Threshold Policy

**Threshold Value**: `Helper.THRESHOLD = 1e-6`

## Problem Statement

In physics simulations with floating-point arithmetic, we face precision issues:
- A ball might calculate to be at position 99.9999999% to the boundary, not exactly at it
- Without careful threshold application, we miss "approximate" collisions and get unintended negative bounces
- Over-applying the threshold creates conflicting logic and masks precision failures

This policy establishes **when** and **how** to use `Helper.THRESHOLD` across geometric and physics code.

---

## Core Principle

**Apply threshold at decision boundaries, not throughout calculations.**

The threshold exists for THREE purposes:
1. **Geometric equivalence** (Point/Line equality) - handle floating-point representation differences
2. **Collision prediction** (is a collision likely to happen?) - add safety margins
3. **Boundary expansion** (is a point inside?) - forgive small precision errors at edges

---

## Rules by Context

### 1. Geometric Comparisons (ALWAYS use threshold)

**When**: Comparing geometric objects for equality or ordering  
**Where**: `Point.equals()`, `Line.equals()`, `Point.compareTo()`, `Line.compareTo()`  
**Why**: Floating-point arithmetic (especially division, square roots) introduces tiny errors. Two geometrically identical points may differ by ±1e-9.  
**How**: Use `Helper.doubleEq(a, b)` for all geometric comparisons.

```java
// ✅ CORRECT - Geometric comparison
return Helper.doubleEq(this.x, other.getX()) && Helper.doubleEq(this.y, other.getY());

// ❌ WRONG - Direct comparison loses precision
return this.x == other.getX() && this.y == other.getY();
```

---

### 2. Geometric Classification (use threshold consistently)

**When**: Classifying or calculating derived properties (slope, midpoint, intersections)  
**Where**: Line.calcSlope(), Line.intersectionWith(), geometry edge cases  
**Why**: A line that deviates from vertical by less than threshold is **essentially vertical**. Consistency: if we're using threshold for geometric equality tests, we must use it for geometric classification too. A slope that's "almost 0 within the threshold" should be treated as 0.  
**How**: Use `Helper.doubleEq()` when classifying geometric properties.

```java
// ✅ CORRECT - If dx is within threshold of 0, treat the line as vertical
if (Helper.doubleEq(dx, 0) && Helper.doubleEq(dy, 0)) {
    return 0;  // Line is a point
}

// ✅ CORRECT - If dx is within threshold of 0, it's vertical
if (Helper.doubleEq(dx, 0)) {
    return Double.POSITIVE_INFINITY;
}

// ❌ WRONG - Exact comparison defeats the purpose of threshold
if (dx == 0) {  // Will miss "nearly vertical" lines
    return Double.POSITIVE_INFINITY;
}

// ❌ WRONG - Don't smooth intermediate calculations for other purposes
double slopeAdjusted = Helper.doubleEq(slope, 0) ? 0 : slope;  // Arbitrary smoothing
```

**Consistency principle**: If Point A and Point B are considered "equal" by `Helper.doubleEq()` (threshold), then a Line from A to B must be classified with threshold awareness too.

---

### 3. Inside/Outside Detection (EXPAND boundaries with threshold)

**When**: Checking if a point is strictly inside or outside a rectangle  
**Where**: `Rectangle.isInXRange()`, `Rectangle.isInYRange()`  
**Why**: A ball's corner point might be calculated as 1e-7 units outside the rectangle due to precision, but logically it should be considered "on the boundary"  
**How**: Expand rectangle boundaries by `Helper.THRESHOLD` for inside/outside tests.

```java
// ✅ CORRECT - Expand bounds to forgive precision errors
private boolean isInXRange(Point p) {
    double x = p.getX();
    return x > leftX() - Helper.THRESHOLD && x < rightX() + Helper.THRESHOLD;
}

// ❌ WRONG - Exact comparison misses nearby points
return x >= leftX() && x <= rightX();
```

**Rationale**: When a ball is moving toward a boundary, we want to catch it just before it would cross. The expansion ensures that points "very close" to the boundary are included.

---

### 4. Collision Prediction (Two-Stage Model B: Coarse Filter → Precise Detection)

**Model**: Two-stage collision detection for efficiency and clarity.

**Stage 1 - Coarse Filtering** (Prediction/early-out): Uses generous threshold to quickly determine "might collide"  
**Stage 2 - Precise Detection** (Actual collision logic): Uses exact logic to determine "definitely collides"

**Why two stages?**
- **Stage 1** reduces false negatives (misses): if there's *any doubt*, let it through to Stage 2
- **Stage 2** ensures accuracy: final collision decision uses rigorous logic, not approximation

**Critical guarantee**: The coarse filter will never *miss* a real collision. It may over-detect (false positives), which is fine — Stage 2 will filter those out.

```java
// ✅ CORRECT - Stage 1: Coarse filter with generous tolerance
// "Is the intersection point anywhere close to reachable this frame?"
if (intersection != null && 
    intersection.distance(leftmost) <= v.getSpeed() + Helper.THRESHOLD) {
    // Might collide - pass to Stage 2
    collisionPtFromR = intersection;
}

// ✅ CORRECT - Stage 2: Precise logic
// "Do we actually collide?"
// (This uses the same exact geometric logic as the final collision calculation)
if (collisionPtFromR != null && /* rigorous condition */) {
    return new Collision(this, /* exact boundaries */);
}

// ❌ WRONG - Using different tolerances at different stages creates bugs
// Stage 1: very permissive
if (intersection.distance(leftmost) <= v.getSpeed() + Helper.THRESHOLD) { }
// Stage 2: suddenly different threshold
if (intersection.distance(leftmost) <= v.getSpeed() - Helper.THRESHOLD) { }
// This can cause: Stage 1 passes it, Stage 2 rejects it (inconsistent)

// ❌ WRONG - Precision loss in Stage 1 causes real collisions to be missed
if (intersection.distance(leftmost) <= v.getSpeed() - Helper.THRESHOLD) {  // TOO STRICT
    // Collision at distance (speed + 1e-7) won't be detected!
}
```

**When to apply threshold in Stage 1 vs Stage 2:**
- **Stage 1 (coarse)**: Add threshold generously; be permissive. Coarse filter's job is "maybe collision" not "definitely".
- **Stage 2 (precise)**: No added tolerance. Use exact geometric logic. Stage 2 is the final arbiter.

**Examples from your code:**
- ✅ Good: `Rectangle.collisionFromOutside()` uses `v.getSpeed() + Helper.THRESHOLD` (Stage 1 coarse filter)
- ✅ Good: `Ball.bounce()` uses exact calculated distances (Stage 2 precision)
- ⚠️ Check: After Stage 1 filtering, Stage 2 must re-verify the collision with exact logic, not just trust the filter

---

### 5. Corner Detection (USE threshold to expand radius)

**When**: Checking if a ball is touching a rectangle corner  
**Where**: `Rectangle.cornerTouch()`  
**Why**: A ball moving diagonally near a corner might have precision errors that place it 1e-7 units away from the corner, missing the collision  
**How**: Expand the radius by `Helper.THRESHOLD` for distance-to-corner checks.

```java
// ✅ CORRECT - Expand radius for fuzzy corner detection
double adjustedR = b.getSize() + Helper.THRESHOLD;
if (c.distance(upperLeft) < adjustedR) {
    return true;
}

// ❌ WRONG - Exact distance requires perfect precision
if (c.distance(upperLeft) <= b.getSize()) {
    // Will miss nearly-touching balls
}
```

---

### 6. Bounce Calculations (DO NOT use threshold)

**When**: Computing the new position and velocity after a collision  
**Where**: `Ball.bounce()`, `Ball.bounceFromLeft()`, `Ball.bounceFromRight()`, etc.  
**Why**: The bounce uses actual measured distances. If we've already detected a collision (step 4), we now work with real positions. Applying threshold here creates overshoot.  
**How**: Use actual double values directly. No threshold application.

```java
// ✅ CORRECT - Use actual values
private void bounceFromLeft(double x) {
    double distanceFromBoundary = x - (point.getX() + radius);
    double absDX = Math.abs(velocity.getDx());
    double newX = x - Math.max((absDX - distanceFromBoundary), radius);
    point = new Point(newX, point.getY());
    velocity.reassign(-absDX, velocity.getDy());
}

// ❌ WRONG - Threshold adjustment after collision detection adds error
double adjustedDistance = distanceFromBoundary > Helper.THRESHOLD ? 
    distanceFromBoundary : 0;
```

**Rationale**: Threshold is a *decision tool*, not a *calculation adjustment*. Once we've decided to bounce, compute exactly.

---

### 7. Multiple Collision Merging (already handles precision)

**When**: Combining collision results from multiple rectangles  
**Where**: `Collision.mergeMultipleCollisions()`, `Collision(Collision, Collision)`  
**Why**: Already uses `max()`/`min()` to select most demanding boundaries; threshold was applied when creating individual collisions  
**How**: Do NOT reapply threshold here. The combining operation preserves precision already applied.

```java
// ✅ CORRECT - Use raw max/min (threshold already applied in individual collisions)
this.fromRightX = Math.max(first.fromRightX, second.fromRightX);
this.fromLeftX = Math.min(first.fromLeftX, second.fromLeftX);

// ❌ WRONG - Double-applying threshold creates inconsistency
this.fromRightX = Math.max(first.fromRightX - Helper.THRESHOLD, 
                            second.fromRightX - Helper.THRESHOLD);
```

---

## Anti-Patterns: What NOT to Do

### ❌ Threshold Infection
Don't sprinkle `Helper.THRESHOLD` throughout calculations:
```java
// Bad - threshold spreads everywhere
double adjustedSlope = slope * (1 + Helper.THRESHOLD);
```

### ❌ Conflicting Thresholds
Don't apply threshold at both detection and bounce stages:
```java
// Bad - creates two competing tolerances
if (collision detected with tolerance)
    newPos = newPos + Helper.THRESHOLD;  // Double tolerance
```

### ❌ Threshold for Performance
Don't use threshold to "smooth" performance issues:
```java
// Bad - masking a real bug
if (Helper.doubleEq(fps, targetFps)) {  // Bad practice
```

---

## Checklist for Threshold Decisions

Before adding `Helper.THRESHOLD` anywhere:

1. **Is this a geometric comparison?** → Use threshold ✅
2. **Am I checking if something is approximately equal?** → Use threshold ✅
3. **Am I expanding a detection boundary?** → Use threshold ✅
4. **Am I computing a position/velocity after collision?** → Do NOT use threshold ❌
5. **Has this value already been checked/expanded elsewhere?** → Do NOT reapply ❌
6. **Am I trying to hide floating-point precision loss?** → Redesign the algorithm ❌

---

## Project-Specific Context

**Why this policy matters for this project:**
- **Physics simulation**: Precise collision detection is critical. Over-tolerance breaks realism (balls pass through walls)
- **Visual rendering**: Pixel coordinates are cast to `int`, so sub-pixel precision is lost anyway
- **Floating-point accumulation**: Many operations (distance, slope, intersection) compound floating-point error
- **Boundary conditions**: The most critical place for threshold is where the ball meets the world edge

**Expected behavior:**
- A ball approaching a boundary should trigger collision detection with slight over-detection
- Once detected, the bounce should position the ball precisely and consistently
- Multiple rectangles should coordinate collisions without threshold conflicts

---

## Overall Strategy: Model B (Coarse Filter → Precise Decision)

The collision system uses a **two-stage approach**:

```
Ball predicts next position
         ↓
    Stage 1: Coarse Filtering (Rectangle.collisionFromOutside/Inside)
    - Uses generous threshold (permissive early-out)
    - Goal: Ensure no real collision is missed
    - OK to over-detect (false positives)
         ↓
    Stage 2: Precise Detection & Bounce (Ball.bounce/Collision logic)
    - Uses exact geometric logic
    - Goal: Calculate exact collision position and response
    - No added tolerance (uses actual distances)
         ↓
    New position and velocity
```

**Guarantee**: If Stage 1 passes the ball through, Stage 2 will either handle a real collision or ignore a false positive. **No collisions are ever missed** because the coarse filter is permissive.

---

## Summary

| Context | Use Threshold? | How | Why |
|---------|---|---|---|
| Geometric equality (Point/Line) | **YES** | `Helper.doubleEq()` | Floating-point precision |
| Geometric classification (slope, vertical?) | **YES** | Check `doubleEq(dx, 0)` etc | Consistency with equality tests |
| Inside/outside detection | **YES** | Expand boundaries by `THRESHOLD` | Forgive boundary precision errors |
| **Stage 1**: Collision coarse filter | **YES** | Add `THRESHOLD` to tolerance | Permissive early-out, no missed collisions |
| **Stage 2**: Collision precise logic | **NO** | Use exact calculated values | Final arbiter, no approximation |
| Corner touch detection | **YES** | Expand radius by `THRESHOLD` | Fuzzy corner detection |
| Bounce calculations | **NO** | Use actual measured values | Precision stage, no smoothing |
| Collision merging | **NO** | Use raw max/min | Already filtered at stage 1 |
| Drawing/UI logic | **NO** | Cast to int; coordinates are discrete | Sub-pixel precision irrelevant |

