import tkinter as tk
from tkinter import messagebox

def change_text(event):
    button1.config(text="Left Clicked!")

def change_color(event):
    button1.config(bg="yellow")

def show_win_message(event):
    messagebox.showinfo("Result", "You Win!")

def show_lose_message(event):
    messagebox.showinfo("Result", "You Lose!")

if __name__ == "__main__":
    # Create the main window
    root = tk.Tk()
    root.title("Button Click Example")

    # Create the first button
    button1 = tk.Button(root, text="Click Me")
    button1.pack(pady=10)

    # Bind events for the first button
    button1.bind("<Button-1>", change_text)  # Left click changes text
    button1.bind("<Button-3>", change_color)  # Right click changes color

    # Create the second button
    button2 = tk.Button(root, text="Result Button")
    button2.pack(pady=10)

    # Bind events for the second button
    button2.bind("<Button-1>", show_win_message)  # Left click shows win message
    button2.bind("<Button-3>", show_lose_message)  # Right click shows lose message

    # Start the Tkinter event loop
    root.mainloop()