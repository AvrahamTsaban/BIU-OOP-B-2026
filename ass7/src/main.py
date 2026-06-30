from board import Board
from gui import GUI

def get_number(prompt):
    while True:
        try:
            value = int(input(prompt))
            return value
        except ValueError:
            print("Please enter a positive integer.")

def main():
    grid_size = get_number("""🎮  Welcome to Minesweeper!
Enter the size of the board (e.g. 10 for 10×10): """)
    mines_number = get_number(f"Enter the number of bombs: ")
    board = Board(grid_size, mines_number)
    gui = GUI(grid_size, board)
    gui.run()

if __name__ == "__main__":
    main()
