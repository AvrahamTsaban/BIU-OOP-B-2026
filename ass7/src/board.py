from secrets import choice
from cell import Cell



class Board:
    def __init__(self, size, mines_amount):
        self.num_of_flags = 0
        self.num_of_revealed_cells = 0
        self.num_of_mines = mines_amount
        self.size = size
        clean_places = list(range(size * size))
        for i in range(mines_amount):
            mine_pos = choice(clean_places)
            clean_places.remove(mine_pos)
        self.grid = self.make_grid(size, clean_places)

    def make_grid(self, size, clean_places):
        grid = [[Cell() if c + r * size in clean_places else Cell(has_mine=True) for c in range(size)] for r in range(size)]
        for r in range(size):
            for c in range(size):
                if grid[r][c].has_mine:
                    grid[r][c].assign_neighbor_mines(99)
                else:
                    neighbor_mines = 0
                    for dr in [-1, 0, 1]:
                        for dc in [-1, 0, 1]:
                            if dr == 0 and dc == 0:
                                continue
                            nr, nc = r + dr, c + dc
                            if 0 <= nr < size and 0 <= nc < size and grid[nr][nc].has_mine:
                                neighbor_mines += 1
                    grid[r][c].assign_neighbor_mines(neighbor_mines)
        return grid

    def check_victory(self):
        return self.num_of_revealed_cells == self.size * self.size - self.num_of_mines

    def reveal_cell(self, row, col):
        if self.grid[row][col].is_flagged():
            # protect the user from accidentally clicking on a flagged cell
            return True
        if self.grid[row][col].is_revealed():
            return True
        if self.grid[row][col].reveal() == False:
            return False
        self.num_of_revealed_cells += 1
        if self.grid[row][col].get_neighbor_mines() == 0:
            for dr in [-1, 0, 1]:
                for dc in [-1, 0, 1]:
                    if dr == 0 and dc == 0:
                        continue
                    nr, nc = row + dr, col + dc
                    if 0 <= nr < self.size and 0 <= nc < self.size:
                        if self.grid[nr][nc].is_flagged():
                            self.flag_cell(nr, nc)
                        self.reveal_cell(nr, nc)
        return True

    def flag_cell(self, row, col):
        cell = self.grid[row][col]
        if cell.is_revealed():
            return False
        if self.num_of_flags >= self.num_of_mines and not cell.is_flagged():
            return False
        if cell.flag():
            self.num_of_flags += 1
        else:
            self.num_of_flags -= 1
        return True

    def get_theorethic_remaining_mines(self):
        return self.num_of_mines - self.num_of_flags