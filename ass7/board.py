from secrets import choice

MINE = 'M'
FLAGGED = 'F'
FALSE_FLAG = 'F'
REVEALED = 'R'
CLEAN_HIDDEN = ' '

class Board:
    def __init__(self, size, mines_amount):
        self.size = size
        clean_places = list(range(size * size))
        for i in range(mines_amount):
            mine_pos = choice(clean_places)
            clean_places.remove(mine_pos)
        self.grid = [[CLEAN_HIDDEN if c + r * size in clean_places else MINE for c in range(size)] for r in range(size)]

    def check_victory(self):
        for r in range(self.size):
            for c in range(self.size):
                if self.grid[r][c] == CLEAN_HIDDEN:
                    return False
        return True

    def reveal_cell(self, row, col):
        if self.grid[row][col] == MINE:
            return False
        if self.grid[row][col] == FLAGGED or self.grid[row][col] == FALSE_FLAG:
            # protect the user from clicking on a flagged cell
            return True
        self.grid[row][col] = REVEALED
        return True