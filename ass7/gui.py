import tkinter as tk

class GUI:
    def __init__(self, grid_size):
        self.root = tk.Tk()
        self.root.title("Minesweeper")
        self.create_widgets()
        for i in range(grid_size):
                for j in range(grid_size):
                    button = tk.Button(self.root, text="", width=3, height=2)
                    button.grid(row=i, column=j)

    def run(self):
        self.root.mainloop()