import tkinter as tk
import tkinter.messagebox as messagebox

class GUI:
    def __init__(self, grid_size, board):
        self.root = tk.Tk()
        self.board = board
        self.root.title("Minesweeper " + str(self.board.get_theorethic_remaining_mines()))
        self.buttons = {}
        for i in range(grid_size):
                for j in range(grid_size):
                    button = tk.Button(self.root, text="", width=3, height=2)
                    button.grid(row=i, column=j)

                    def left_click(event, row=i, col=j, button=button):
                        if self.board.reveal_cell(row, col):
                            self.refresh_board()
                            if self.board.check_victory():
                                self.victory()
                        else:
                            button.config(text="B", bg="red")
                            self.game_over()

                    def right_click(event, row=i, col=j, button=button):
                        if self.board.flag_cell(row, col):
                            if self.board.grid[row][col].is_flagged():
                                button.config(text="F", bg="yellow")
                            else:
                                button.config(text="", bg="lightgray")
                    
                    button.bind("<Button-1>", left_click)
                    button.bind("<Button-3>", right_click)
                    self.buttons[(i, j)] = button
        self.refresh_board()

    def refresh_board(self):
        for i in range(self.board.size):
            for j in range(self.board.size):
                cell = self.board.grid[i][j]
                button = self.buttons[(i, j)]
                if cell.is_revealed():
                    if cell.has_mine:
                        button.config(text="B", bg="red")
                    else:
                        num = cell.get_neighbor_mines()
                        if num == 0:
                            button.config(text="", bg="white")
                        else:
                            button.config(text=str(cell.get_neighbor_mines()), bg="white")
                elif cell.is_flagged():
                    button.config(text="F", bg="yellow")
                else:
                    button.config(text="", bg="lightgray")
        self.root.title("Minesweeper" + str(self.board.get_theorethic_remaining_mines()))

    def run(self):
        self.root.mainloop()

    def victory(self):
        messagebox.showinfo("Victory", "You won the game!")
        self.root.destroy()

    def game_over(self):
        messagebox.showinfo("Game Over", "You hit a mine!")
        self.root.destroy()
