from gui import GUI

def get_number(prompt, min_value, max_value):
    while True:
        try:
            value = int(input(prompt))
            if min_value <= value <= max_value:
                return value
            else:
                print(f"Please enter a number between {min_value} and {max_value}.")
        except ValueError:
            print("Invalid input. Please enter a valid integer.")

def main():
    grid_size = get_number("Enter the grid size (5-20): ", 5, 20)
    mines_number = get_number(f"Enter the number of mines (1-{int(grid_size * grid_size / 3)}): ", 1, int(grid_size * grid_size / 3))
    gui = GUI(grid_size)
    gui.run()

if __name__ == "__main__":
    main()
