package com.ondevice.mat.tests

import android.util.Log
import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import com.ondevice.mat.automation.NodeInfo
import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis

class SudokuTest : Test() {

    override val packageName = "com.divinememorygames.super.killer.free.sudoku.master"

    enum class Inputs {
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE
    }

    enum class Difficulties {
        BEGINNER, EASY, MEDIUM, HARD
    }

    override suspend fun runTests() {
        super.runTests()

        if (engine != null) {
            Log.v(OUTPUT_TAG, "--- Starting Automatic Sudoku Test ---")
            val executionTime = measureTimeMillis {
                Log.v(OUTPUT_TAG, "--- Starting beginner difficulty ---")
                beginnerDiffTest()

                Log.v(OUTPUT_TAG, "--- Starting easy difficulty ---")
                easyDiffTest()

                Log.v(OUTPUT_TAG, "--- Starting medium difficulty ---")
                mediumDiffTest()

                Log.v(OUTPUT_TAG, "--- Starting hard difficulty ---")
                hardDiffTest()
            }
            Log.v(
                OUTPUT_TAG,
                "--- Automatic Sudoku Test Finished 4 difficulties in ${executionTime / 1000.0} seconds---"
            )
        }
    }

    private suspend fun beginnerDiffTest() {

        if (!clickNewPuzzle()) {
            return
        }
        if (!selectDifficulty(Difficulties.BEGINNER)) {
            return
        }
        if (!startPuzzle()) {
            return
        }

        val nodeBoard = collectBoard() ?: return
        val board = convertBoard(nodeBoard)

        if (solveSudoku(board)) {
            val sudokuFinished = fillInSudoku(board, nodeBoard)

            if (!sudokuFinished) {
                return
            }

            engine?.pressBack(Pair("New_Game", Engine.searchTypes.RESOURCE_ID))
        }

    }

    private suspend fun easyDiffTest() {
        if (!clickNewPuzzle()) {
            return
        }
        if (!selectDifficulty(Difficulties.EASY)) {
            return
        }
        if (!startPuzzle()) {
            return
        }

        val nodeBoard = collectBoard() ?: return
        val board = convertBoard(nodeBoard)

        if (solveSudoku(board)) {
            val sudokuFinished = fillInSudoku(board, nodeBoard)

            if (!sudokuFinished) {
                return
            }

            engine?.pressBack(Pair("New_Game", Engine.searchTypes.RESOURCE_ID))
        }
    }

    private suspend fun mediumDiffTest() {
        if (!clickNewPuzzle()) {
            return
        }
        if (!selectDifficulty(Difficulties.MEDIUM)) {
            return
        }
        if (!startPuzzle()) {
            return
        }

        val nodeBoard = collectBoard() ?: return
        val board = convertBoard(nodeBoard)

        if (solveSudoku(board)) {
            val sudokuFinished = fillInSudoku(board, nodeBoard)

            if (!sudokuFinished) {
                return
            }

            engine?.pressBack(Pair("New_Game", Engine.searchTypes.RESOURCE_ID))
        }
    }

    private suspend fun hardDiffTest() {
        if (!clickNewPuzzle()) {
            return
        }
        if (!selectDifficulty(Difficulties.HARD)) {
            return
        }
        if (!startPuzzle()) {
            return
        }

        val nodeBoard = collectBoard() ?: return
        val board = convertBoard(nodeBoard)

        if (solveSudoku(board)) {
            val sudokuFinished = fillInSudoku(board, nodeBoard)

            if (!sudokuFinished) {
                return
            }

            engine?.pressBack(Pair("New_Game", Engine.searchTypes.RESOURCE_ID))
        }
    }

    private suspend fun clickNewPuzzle(): Boolean {
        val resourceId = "New_Game"
        val target = Pair("play", Engine.searchTypes.RESOURCE_ID)
        val newPuzzleButton = engine?.retrieveNode(resourceId, Engine.searchTypes.RESOURCE_ID) ?: return false

        return engine?.click(newPuzzleButton, target) ?: false
    }

    private suspend fun startPuzzle(): Boolean {
        val resourceId = "play"
        val target = Pair("button_1", Engine.searchTypes.RESOURCE_ID)
        val playButton = engine?.retrieveNode(resourceId, Engine.searchTypes.RESOURCE_ID) ?: return false

        return engine?.click(playButton, target) ?: false
    }

    private suspend fun selectDifficulty(difficulty: Difficulties): Boolean {
        val textField = when (difficulty) {
            Difficulties.BEGINNER -> "BEGINNER"
            Difficulties.EASY -> "EASY"
            Difficulties.MEDIUM -> "MEDIUM"
            Difficulties.HARD -> "HARD"
        }
        val target = Pair("play", Engine.searchTypes.RESOURCE_ID)

        val innerNode = engine?.retrieveNode(textField, Engine.searchTypes.TEXT) ?: return false
        val clickableNode = innerNode.getParent()
        if (clickableNode != null) {
            return engine?.click(clickableNode, target) ?: false
        }
        return false
    }

    private suspend fun clickInputButton(input: Inputs): Boolean {
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

        val target = Pair("button_1", Engine.searchTypes.RESOURCE_ID)
        val inputButton = engine?.retrieveNode(resourceId, Engine.searchTypes.RESOURCE_ID) ?: return false

        return engine?.click(inputButton, target) ?: false

    }

    private suspend fun clickSquare(nodeInfo: NodeInfo): Boolean {
        val target = Pair("button_1", Engine.searchTypes.RESOURCE_ID)
        return engine?.click(nodeInfo, target, coordinates = true) ?: false
    }

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

    private fun convertBoard(nodeBoard: Array<Array<NodeInfo?>>): Array<IntArray> {
        val board: Array<IntArray> = Array(9) { IntArray(9) { 0 } }

        for (i in nodeBoard.indices) {
            for (j in 0..<nodeBoard[i].size) {
                val square = nodeBoard[i][j] ?: continue
                if (square.nodeText() == "") {
                    continue
                }
                board[i][j] = square.nodeText().toInt()
            }
        }

        return board

    }

    private fun collectBoard(): Array<Array<NodeInfo?>>? {
        val board: Array<Array<NodeInfo?>> = Array(9) { Array(9) { null } }

        val tableId = "tableId"
        val fullBoardNode =
            engine?.retrieveNode(tableId, Engine.searchTypes.RESOURCE_ID, allNodes = true) ?: return null

        var curRow = 0

        for (i in 0 until fullBoardNode.childCount()) {

            if (i == 3 || i == 7) {
                continue
            }

            val nodeRow = fullBoardNode.getChild(i) ?: return null
            var curCol = 0

            for (j in 0 until nodeRow.childCount()) {

                if (j == 3 || j == 7) {
                    continue
                }

                board[curRow][curCol] = nodeRow.getChild(j) ?: return null

                curCol += 1
            }
            curRow += 1
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

    private fun getNumberOfEmptySquares(nodeBoard: Array<Array<NodeInfo?>>): Int {

        var emptySum = 0

        for (row in nodeBoard.indices) {
            for (col in nodeBoard[row].indices) {
                val square = nodeBoard[row][col] ?: continue
                if (square.nodeText() == "") {
                    emptySum++
                }
            }
        }

        return emptySum

    }

    private suspend fun fillInSudoku(board: Array<IntArray>, nodeBoard: Array<Array<NodeInfo?>>): Boolean {

        var emptySquares = getNumberOfEmptySquares(nodeBoard)

        for (row in nodeBoard.indices) {
            for (col in nodeBoard[row].indices) {
                val square = nodeBoard[row][col] ?: return false
                if (square.nodeText() == "") {

                    val squareClicked = clickSquare(square)

                    if (!squareClicked) {
                        return false
                    }

                    val value = board[row][col]
                    val inputValue = getInput(value) ?: return false


                    val filledSquare = clickInputButton(inputValue)

                    if (!filledSquare && emptySquares > 1) {
                        return false
                    } else if (!filledSquare) {
                        delay(500)
                        return true
                    }
                    emptySquares--
                }
            }
        }
        return true
    }
}