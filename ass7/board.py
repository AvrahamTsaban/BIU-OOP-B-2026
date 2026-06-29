from secrets import choice
from cell import Cell



class Board:
    def __init__(self, size, mines_amount):
        self.num_of_flags = 0
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
                if not grid[r][c].has_mine:
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
        for r in range(self.size):
            for c in range(self.size):
                if self.grid[r][c].is_hidden and not self.grid[r][c].has_mine:
                    return False
        return True

    def reveal_cell(self, row, col):
        if self.grid[row][col].is_flagged:
            # protect the user from accidentally clicking on a flagged cell
            return True
        if self.grid[row][col].reveal() == False:
            return False
        if self.grid[row][col].get_neighbor_mines() == 0:
            for dr in [-1, 0, 1]:
                for dc in [-1, 0, 1]:
                    if dr == 0 and dc == 0:
                        continue
                    nr, nc = row + dr, col + dc
                    if 0 <= nr < self.size and 0 <= nc < self.size:
                        self.reveal_cell(nr, nc)
        return True

    def flag_cell(self, row, col):
        if self.num_of_flags >= self.num_of_mines or self.grid[row][col].is_revealed():
            return False
        if self.grid[row][col].flag():
            self.num_of_flags += 1
        else:
            self.num_of_flags -= 1
        return True