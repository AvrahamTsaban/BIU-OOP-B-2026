import tkinter as tk

class GUI:
    def __init__(self, grid_size, board):
        self.root = tk.Tk()
        self.root.title("Minesweeper")
        # self.create_widgets()
        self.board = board
        self.buttons = {}
        for i in range(grid_size):
                for j in range(grid_size):
                    button = tk.Button(self.root, text="", width=3, height=2)
                    button.grid(row=i, column=j)
                    button.config(bg="lightgray")

                    def left_click(event, row=i, col=j):
                        if self.board.reveal_cell(row, col):
                            self.refresh_board()
                            if self.board.check_victory():
                                self.show_message("You won the game!")
                                self.game.victory()
                        else:
                            button.config(text="B", bg="red")
                            self.show_message("You hit a mine!")
                            self.game.game_over()

                    def right_click(event, row=i, col=j):
                        if self.board.flag_cell(row, col):
                            if self.board.grid[row][col].is_flagged:
                                button.config(text="F", bg="yellow")
                            else:
                                button.config(text="", bg="lightgray")
                    
                    button.bind("<Button-1>", left_click)
                    button.bind("<Button-3>", right_click)
                    self.buttons[(i, j)] = button

    def refresh_board(self):
        for i in range(self.board.size):
            for j in range(self.board.size):
                cell = self.board.grid[i][j]
                button = self.buttons[(i, j)]
                if cell.is_revealed():
                    if cell.has_mine:
                        button.config(text="B", bg="red")
                    else:
                        button.config(text=str(cell.get_neighbor_mines()), bg="white")
                elif cell.is_flagged:
                    button.config(text="F", bg="yellow")
                else:
                    button.config(text="", bg="lightgray")

    def run(self):
        self.root.mainloop()