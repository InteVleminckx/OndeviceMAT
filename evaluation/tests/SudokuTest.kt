package com.ondevice.offdevicetesting

import androidx.test.uiautomator.UiObject2
import kotlinx.coroutines.delay
import org.junit.Test

class SudokuTest : BaseTestClass("com.divinememorygames.super.killer.free.sudoku.master") {

    enum class Inputs {
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE
    }

    enum class Difficulties {
        BEGINNER, EASY, MEDIUM, HARD
    }

    @Test
    fun sudoku() {
        for (i in 0 until 6) {
            executeTest(::allDifficulties, 4, "Sudoku four difficulties test")
            createNewFile()
        }
    }

    private fun allDifficulties(iteration: Int): Pair<Boolean, String> {
        var result = beginnerDiffTest()
        if (!result.first) return Pair(false, result.second)
        result = easyDiffTest()

        if (!result.first) return Pair(false, result.second)
        result = mediumDiffTest()

        if (!result.first) return Pair(false, result.second)
        result = hardDiffTest()

        if (!result.first) return Pair(false, result.second)

        return Pair(true, "Successfully finished the sudoku test")
    }

    private fun beginnerDiffTest(): Pair<Boolean, String> {
        var result = clickNewPuzzle()
        if (!result.first) return result
        result = selectDifficulty(Difficulties.BEGINNER)
        if (!result.first) return result
        result = startPuzzle()
        if (!result.first) return result

        val boardResult = collectBoard()

        val nodeBoard = boardResult.first ?: return Pair(false, boardResult.second)
        val board = convertBoard(nodeBoard)

        if (solveSudoku(board)) {
            val sudokuFinished = fillInSudoku(board, nodeBoard)

            if (!sudokuFinished.first) {
                return sudokuFinished
            }
            Thread.sleep(500)
            device.pressBack()
        }

        return Pair(true, "Successfully finished beginner sudoku")
    }

    private fun easyDiffTest(): Pair<Boolean, String> {
        var result = clickNewPuzzle()
        if (!result.first) return result
        result = selectDifficulty(Difficulties.EASY)
        if (!result.first) return result
        result = startPuzzle()
        if (!result.first) return result

        val boardResult = collectBoard()

        val nodeBoard = boardResult.first ?: return Pair(false, boardResult.second)
        val board = convertBoard(nodeBoard)

        if (solveSudoku(board)) {
            val sudokuFinished = fillInSudoku(board, nodeBoard)

            if (!sudokuFinished.first) {
                return sudokuFinished
            }
            Thread.sleep(500)

            device.pressBack()
        }

        return Pair(true, "Successfully finished easy sudoku")
    }

    private fun mediumDiffTest(): Pair<Boolean, String> {
        var result = clickNewPuzzle()
        if (!result.first) return result
        result = selectDifficulty(Difficulties.MEDIUM)
        if (!result.first) return result
        result = startPuzzle()
        if (!result.first) return result

        val boardResult = collectBoard()

        val nodeBoard = boardResult.first ?: return Pair(false, boardResult.second)
        val board = convertBoard(nodeBoard)

        if (solveSudoku(board)) {
            val sudokuFinished = fillInSudoku(board, nodeBoard)

            if (!sudokuFinished.first) {
                return sudokuFinished
            }
            Thread.sleep(500)

            device.pressBack()
        }

        return Pair(true, "Successfully finished medium sudoku")
    }

    private fun hardDiffTest(): Pair<Boolean, String> {
        var result = clickNewPuzzle()
        if (!result.first) return result
        result = selectDifficulty(Difficulties.HARD)
        if (!result.first) return result
        result = startPuzzle()
        if (!result.first) return result

        val boardResult = collectBoard()

        val nodeBoard = boardResult.first ?: return Pair(false, boardResult.second)
        val board = convertBoard(nodeBoard)

        if (solveSudoku(board)) {
            val sudokuFinished = fillInSudoku(board, nodeBoard)

            if (!sudokuFinished.first) {
                return sudokuFinished
            }
            Thread.sleep(500)

            device.pressBack()
        }

        return Pair(true, "Successfully finished hard sudoku")
    }

    private fun clickNewPuzzle(): Pair<Boolean, String> {
        return clickObjectByRes(getResId("New_Game"))
    }

    private fun startPuzzle(): Pair<Boolean, String> {
        return clickObjectByRes(getResId("play"))
    }

    private fun selectDifficulty(difficulty: Difficulties): Pair<Boolean, String> {
        val textField = when (difficulty) {
            Difficulties.BEGINNER -> "BEGINNER"
            Difficulties.EASY -> "EASY"
            Difficulties.MEDIUM -> "MEDIUM"
            Difficulties.HARD -> "HARD"
        }

        return clickObjectByText(textField)
    }

    private fun clickInputButton(input: Inputs): Pair<Boolean, String> {
        val resourceId = when (input) {
            Inputs.ONE -> "button_1"
            Inputs.TWO -> "button_2"
            Inputs.THREE -> "button_3"
            Inputs.FOUR -> "button_4"
            Inputs.FIVE -> "button_5"
            Inputs.SIX -> "button_6"
            Inputs.SEVEN -> "button_7"
            Inputs.EIGHT -> "button_8"
            Inputs.NINE -> "button_9"
        }

        return clickObjectByRes(getResId(resourceId))
    }

//    private suspend fun clickSquare(nodeInfo: NodeInfo): Boolean {
//        val target = Pair("button_1", Engine.searchTypes.RESOURCE_ID)
//        return engine?.click(nodeInfo, target, coordinates = true) ?: false
//    }

    private fun getInput(value: Int): Inputs? {
        return when (value) {
            1 -> Inputs.ONE
            2 -> Inputs.TWO
            3 -> Inputs.THREE
            4 -> Inputs.FOUR
            5 -> Inputs.FIVE
            6 -> Inputs.SIX
            7 -> Inputs.SEVEN
            8 -> Inputs.EIGHT
            9 -> Inputs.NINE
            else -> null
        }
    }

    private fun collectBoard(): Pair<Array<Array<UiObject2?>>?, String> {

        val board: Array<Array<UiObject2?>> = Array(9) { Array(9) { null } }

        val fullBoard = getNodeByRes(getResId("tableId")) ?: return Pair(
            null,
            "Failed to retrieve the full board"
        )

        var curRow = 0

        for (i in 0 until fullBoard.childCount) {

            if (i == 3 || i == 7) {
                continue
            }

            val nodeRow = fullBoard.children[i] ?: return Pair(
                null,
                "List index out of range when retrieving child $i of the board"
            )
            var curCol = 0

            for (j in 0 until nodeRow.childCount) {

                if (j == 3 || j == 7) {
                    continue
                }

                board[curRow][curCol] = nodeRow.children[j] ?: return Pair(
                    null,
                    "List index out of range when retrieving child $j of board row $i"
                )

                curCol += 1
            }
            curRow += 1
        }

        return Pair(board, "Successfully collected the board.")

    }

    private fun convertBoard(nodeBoard: Array<Array<UiObject2?>>): Array<IntArray> {
        val board: Array<IntArray> = Array(9) { IntArray(9) { 0 } }

        for (i in nodeBoard.indices) {
            for (j in 0..<nodeBoard[i].size) {
                val square = nodeBoard[i][j] ?: continue
                if (square.text == "" || square.text == null) {
                    continue
                }
                board[i][j] = square.text.toInt()
            }
        }

        return board

    }

    private fun solveSudoku(board: Array<IntArray>): Boolean {
        for (row in board.indices) {
            for (col in board[row].indices) {
                if (board[row][col] == 0) {
                    for (num in 1..9) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num
                            if (solveSudoku(board)) {
                                return true
                            }
                            board[row][col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun isSafe(board: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        return !isInRow(board, row, num) &&
                !isInColumn(board, col, num) &&
                !isInBox(board, row - row % 3, col - col % 3, num)
    }

    private fun isInRow(board: Array<IntArray>, row: Int, num: Int): Boolean {
        for (col in board[row].indices) {
            if (board[row][col] == num) {
                return true
            }
        }
        return false
    }

    private fun isInColumn(board: Array<IntArray>, col: Int, num: Int): Boolean {
        for (row in board.indices) {
            if (board[row][col] == num) {
                return true
            }
        }
        return false
    }

    private fun isInBox(board: Array<IntArray>, startRow: Int, startCol: Int, num: Int): Boolean {
        for (row in 0 until 3) {
            for (col in 0 until 3) {
                if (board[row + startRow][col + startCol] == num) {
                    return true
                }
            }
        }
        return false
    }

    private fun getNumberOfEmptySquares(nodeBoard: Array<Array<UiObject2?>>): Int {

        var emptySum = 0

        for (row in nodeBoard.indices) {
            for (col in nodeBoard[row].indices) {
                val square = nodeBoard[row][col] ?: continue
                if (square.text == "" || square.text == null) {
                    emptySum++
                }
            }
        }

        return emptySum

    }

    private fun fillInSudoku(
        board: Array<IntArray>,
        nodeBoard: Array<Array<UiObject2?>>
    ): Pair<Boolean, String> {

        var emptySquares = getNumberOfEmptySquares(nodeBoard)

        for (row in nodeBoard.indices) {
            for (col in nodeBoard[row].indices) {
                val square = nodeBoard[row][col] ?: return Pair(
                    false,
                    "Couldn't retrieve a square at position ($row, $col)"
                )
                if (square.text == "" || square.text == null) {

                    square.click()

                    val value = board[row][col]
                    val inputValue =
                        getInput(value) ?: return Pair(false, "No input found for value: $value")


                    val filledSquareResult = clickInputButton(inputValue)

                    if (!filledSquareResult.first) {
                        return filledSquareResult
                    }

                    logToFile(filledSquareResult.second)

                    emptySquares--

                    if (emptySquares == 0) {
                        break
                    }
                }
            }
        }
        return Pair(true, "Successfully filled in the sudoku")
    }

}