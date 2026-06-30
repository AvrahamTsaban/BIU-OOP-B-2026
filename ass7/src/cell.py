class Cell:
    def __init__(self, has_mine=False, state="hidden"):
        self.has_mine = has_mine
        self.state = state
        self.neighbor_mines = 0

    def is_hidden(self):
        return self.state == "hidden"

    def is_flagged(self):
        return self.state == "flagged"

    def assign_neighbor_mines(self, count):
        self.neighbor_mines = count

    def get_neighbor_mines(self):
        return self.neighbor_mines

    def reveal(self):
        if self.is_flagged():
            return True
        if self.has_mine:
            return False
        self.state = "revealed"
        return True

    def flag(self):
        if self.is_hidden():
            self.state = "flagged"
        elif self.is_flagged():
            self.state = "hidden"
        return self.is_flagged()

    def is_revealed(self):
        return self.state == "revealed"