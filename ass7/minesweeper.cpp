/*
 * created and maintained by LLMs, to demonstrate the idea of
 * translating simple to write langs to power-efficient langs.
 * (It creates a code not optimised to target language.
 * for example, I used python which has very versatile handling
 * of variables. The resulting code is full of castings.)
 * compile with
 * g++ -std=c++23 minesweeper.cpp -o minesweeper -I/usr/include/qt6 -I/usr/include/qt6/QtWidgets -I/usr/include/qt6/QtCore -I/usr/include/qt6/QtGui -lQt6Widgets -lQt6Core -lQt6Gui
 * or
 * g++ -std=c++23 minesweeper.cpp -o minesweeper $(pkg-config --cflags --libs Qt6Widgets)
*/
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <random>
#include <map>
#include <functional>
#include <stdexcept>
#include <QtWidgets/QApplication>
#include <QtWidgets/QWidget>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QGridLayout>
#include <QtWidgets/QMessageBox>
#include <QMouseEvent>

// --- 1. CELL CLASS ---
class Cell {
public:
    enum class State { Hidden, Flagged, Revealed };

    bool has_mine{false};
    State state{State::Hidden};
    int neighbor_mines{0};

    Cell() = default;
    explicit Cell(bool mine) noexcept : has_mine(mine) {}

    bool is_hidden() const noexcept { return state == State::Hidden; }
    bool is_flagged() const noexcept { return state == State::Flagged; }
    bool is_revealed() const noexcept { return state == State::Revealed; }

    void assign_neighbor_mines(int count) noexcept { neighbor_mines = count; }
    int get_neighbor_mines() const noexcept { return neighbor_mines; }

    bool reveal() noexcept {
        if (is_flagged()) return true;
        state = State::Revealed;
        if (has_mine) return false;
        return true;
    }

    bool flag() noexcept {
        if (is_hidden()) {
            state = State::Flagged;
        } else if (is_flagged()) {
            state = State::Hidden;
        }
        return is_flagged();
    }
};

// --- 2. BOARD CLASS ---
class Board {
public:
    int num_of_flags{0};
    int num_of_revealed_cells{0};
    int num_of_mines{0};
    int size{0};
    std::vector<std::vector<Cell>> grid;

    explicit Board(int size_, int mines_amount) {
        if (size_ <= 0 || mines_amount < 0 || mines_amount >= size_ * size_) {
            throw std::invalid_argument("invalid board size or mines amount");
        }
        num_of_flags = 0;
        num_of_revealed_cells = 0;
        num_of_mines = mines_amount;
        size = size_;

        std::vector<int> clean_places(size * size);
        for (int i = 0; i < size * size; ++i) clean_places[i] = i;

        std::random_device rd;
        std::mt19937 g(rd());
        std::shuffle(clean_places.begin(), clean_places.end(), g);
        clean_places.resize(size * size - mines_amount);

        grid = make_grid(size, clean_places);
    }

    std::vector<std::vector<Cell>> make_grid(int size_, const std::vector<int>& clean_places) {
        std::vector<std::vector<Cell>> res_grid(size_, std::vector<Cell>(size_));
        std::vector<char> is_clean(size_ * size_, 0);
        for (int idx : clean_places) {
            if (0 <= idx && idx < size_ * size_) is_clean[idx] = 1;
        }

        for (int r = 0; r < size_; ++r) {
            for (int c = 0; c < size_; ++c) {
                int pos = c + r * size_;
                bool clean = is_clean[pos];
                res_grid[r][c] = Cell(!clean);
            }
        }

        for (int r = 0; r < size_; ++r) {
            for (int c = 0; c < size_; ++c) {
                if (res_grid[r][c].has_mine) {
                    res_grid[r][c].assign_neighbor_mines(99);
                } else {
                    int neighbor_mines = 0;
                    for (int dr : {-1, 0, 1}) {
                        for (int dc : {-1, 0, 1}) {
                            if (dr == 0 && dc == 0) continue;
                            int nr = r + dr, nc = c + dc;
                            if (0 <= nr && nr < size_ && 0 <= nc && nc < size_ && res_grid[nr][nc].has_mine) {
                                neighbor_mines++;
                            }
                        }
                    }
                    res_grid[r][c].assign_neighbor_mines(neighbor_mines);
                }
            }
        }
        return res_grid;
    }

    bool check_victory() const noexcept {
        return num_of_revealed_cells == size * size - num_of_mines;
    }

    bool reveal_cell(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) return true;
        if (grid[row][col].is_flagged()) return true;
        if (grid[row][col].is_revealed()) return true;
        if (!grid[row][col].reveal()) return false;

        ++num_of_revealed_cells;
        if (grid[row][col].get_neighbor_mines() == 0) {
            for (int dr : {-1, 0, 1}) {
                for (int dc : {-1, 0, 1}) {
                    if (dr == 0 && dc == 0) continue;
                    int nr = row + dr, nc = col + dc;
                    if (0 <= nr && nr < size && 0 <= nc && nc < size) {
                        if (grid[nr][nc].is_flagged()) {
                            flag_cell(nr, nc);
                        }
                        reveal_cell(nr, nc);
                    }
                }
            }
        }
        return true;
    }

    bool flag_cell(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) return false;
        Cell& cell = grid[row][col];
        if (cell.is_revealed()) return false;
        if (num_of_flags >= num_of_mines && !cell.is_flagged()) return false;
        if (cell.flag()) {
            ++num_of_flags;
        } else {
            --num_of_flags;
        }
        return true;
    }

    int get_theoretic_remaining_mines() const noexcept {
        return num_of_mines - num_of_flags;
    }
};

// --- 3. CUSTOM BUTTON TO HANDLE RIGHT CLICK ---
class MinesweeperButton : public QPushButton {
public:
    std::function<void()> onLeftClick;
    std::function<void()> onRightClick;

    MinesweeperButton(QWidget* parent = nullptr) : QPushButton(parent) {
        setFixedSize(40, 40); // קיבוע גודל מוחלט למניעת שינוי רוחב עמודות
    }

protected:
    void mousePressEvent(QMouseEvent* event) override {
        if (event->button() == Qt::LeftButton && onLeftClick) {
            onLeftClick();
        } else if (event->button() == Qt::RightButton && onRightClick) {
            onRightClick();
        }
        QPushButton::mousePressEvent(event);
    }
};

// --- 4. GUI CLASS ---
class GUI {
public:
    QWidget* root;
    Board& board;
    std::map<std::pair<int, int>, MinesweeperButton*> buttons;

    GUI(int grid_size, Board& board_) : board(board_) {
        root = new QWidget();
        root->setWindowTitle(QString("Minesweeper ") + QString::number(board.get_theoretic_remaining_mines()));

        QGridLayout* layout = new QGridLayout(root);
        layout->setSpacing(1);
        layout->setSizeConstraint(QLayout::SetFixedSize); // מאלץ את החלון כולו להישאר בגודל הכללי הנכון

        for (int i = 0; i < grid_size; ++i) {
            for (int j = 0; j < grid_size; ++j) {
                MinesweeperButton* button = new MinesweeperButton(root);
                layout->addWidget(button, i, j);

                button->onLeftClick = [this, i, j, button]() {
                    if (this->board.reveal_cell(i, j)) {
                        this->refresh_board();

                        if (this->board.check_victory()) {
                            QCoreApplication::processEvents();
                            this->victory();
                        }
                    } else {
                        // מעדכנים את הלוח כדי שיראו את הפצצה *לפני* שהמשחק נגמר
                        this->refresh_board();
                        button->setText("B");
                        button->setStyleSheet("background-color: red; color: black; font-weight: bold; border: 1px solid #777777; border-radius: 0px;");
                        QCoreApplication::processEvents();
                        
                        this->game_over();
                    }
                };

                button->onRightClick = [this, i, j, button]() {
                    if (this->board.flag_cell(i, j)) {
                        this->refresh_board();
                    }
                };

                buttons[{i, j}] = button;
            }
        }
        refresh_board();
    }

    void refresh_board() {
        for (int i = 0; i < board.size; ++i) {
            for (int j = 0; j < board.size; ++j) {
                Cell& cell = board.grid[i][j];
                MinesweeperButton* button = buttons[{i, j}];

                if (cell.is_revealed()) {
                    if (cell.has_mine) {
                        button->setText("B");
                        button->setStyleSheet("background-color: red; color: black; font-weight: bold; border: 1px solid #777777; border-radius: 0px;");
                    } else {
                        int num = cell.get_neighbor_mines();
                        if (num == 0) {
                            button->setText("");
                            button->setStyleSheet("background-color: white; border: 1px solid #d3d3d3; border-radius: 0px;");
                        } else {
                            button->setText(QString::number(num));
                            button->setStyleSheet("background-color: white; color: blue; font-weight: bold; border: 1px solid #d3d3d3; border-radius: 0px;");
                        }
                    }
                } else if (cell.is_flagged()) {
                    button->setText("F");
                    button->setStyleSheet("background-color: yellow; color: black; font-weight: bold; border: 1px solid #777777; border-radius: 0px;");
                } else {
                    button->setText("");
                    button->setStyleSheet("background-color: lightgray; border: 1px solid #777777; border-radius: 0px;");
                }
            }
        }
        root->setWindowTitle(QString("Minesweeper ") + QString::number(board.get_theoretic_remaining_mines()));

        // כפיית ריענון מיידי של הממשק הגרפי לפני הקפיצה של תיבת הדיאלוג
        QCoreApplication::processEvents();
    }

    void run() {
        root->show();
    }

    void victory() {
        QMessageBox::information(root, "Victory", "You won the game!");
        root->close();
    }

    void game_over() {
        QMessageBox::information(root, "Game Over", "You hit a mine!");
        root->close();
    }
};

// --- 5. MAIN FUNCTION ---
int get_number(const std::string& prompt) {
    while (true) {
        std::cout << prompt;
        std::string input;
        if (!std::getline(std::cin, input)) return 0;
        try {
            int value = std::stoi(input);
            if (value <= 0) throw std::invalid_argument("");
            return value;
        } catch (...) {
            std::cout << "Please enter a positive integer.\n";
        }
    }
}

int main(int argc, char* argv[]) {
    QApplication app(argc, argv);

    std::cout << "🎮  Welcome to Minesweeper!\n";
    int grid_size = get_number("Enter the size of the board (e.g. 10 for 10x10): ");
    int mines_number = get_number("Enter the number of bombs: ");

    Board board(grid_size, mines_number);
    GUI gui(grid_size, board);
    gui.run();

    return app.exec();
}
