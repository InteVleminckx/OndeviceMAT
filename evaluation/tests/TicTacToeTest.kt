package com.ondevice.offdevicetesting

import org.junit.Test

class TicTacToeTest : BaseTestClass("ug.lecode.tictactoe") {

    @Test
    fun ticTactoe() {
        executeTest(::automaticTicTacToe, 1000, "Tic Tac Toe automation test")
    }


    private fun automaticTicTacToe(iterations: Int): Pair<Boolean, String> {

        val human = "X"
        val phone = "0"

        for (i in 0 until iterations) {

            val boardResult = updateBoard()
            val board = boardResult.first ?: return Pair(false, boardResult.second)

            // Check if there is a winning move
            val winningMove = getBestMove(human, board)
            if (winningMove != null) {
                board[winningMove.first][winningMove.second] = human
                val result = setMove(winningMove)
                if (!result.first) return result
                continue
            }

            // Check if the phone has a winning move, and block him
            val blockingMove = getBestMove(phone, board)
            if (blockingMove != null) {
                board[blockingMove.first][blockingMove.second] = human
                val result = setMove(blockingMove)
                if (!result.first) return result
                continue
            }

            // No winning or blocking move, place in a random corner or center
            val emptyFields = getEmptyFields(board)
            val preferredPlaces = listOf(Pair(0, 0), Pair(0, 2), Pair(1, 1), Pair(2, 0), Pair(2, 2))
            val preferredMove = preferredPlaces.firstOrNull { it in emptyFields }

            val move = preferredMove ?: emptyFields.random()
            board[move.first][move.second] = human
            val result = setMove(move)
            if (!result.first) return result

        }

        return Pair(true, "Successfully executed the tic tac toe test.")

    }

    private fun setMove(position: Pair<Int, Int>): Pair<Boolean, String> {
        val resourceId: String = getIdResource(position.first, position.second)
        return clickObjectByRes(getResId(resourceId))

    }

    private fun getIdResource(row: Int, col: Int): String {

        val ids = arrayOf(
            arrayOf("one", "two", "three"),
            arrayOf("four", "five", "six"),
            arrayOf("seven", "eight", "nine")
        )
        return ids[row][col]
    }

    private fun getEmptyFields(board: Array<Array<String>>): List<Pair<Int, Int>> {

        val emptyMoves = mutableListOf<Pair<Int, Int>>()

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (board[i][j] == "") {
                    emptyMoves.add(Pair(i, j))
                }
            }
        }

        return emptyMoves
    }

    private fun getBestMove(marker: String, board: Array<Array<String>>): Pair<Int, Int>? {
        // Check for winning move in rows
        for (i in 0 until 3) {
            val row = board[i]
            if (row.count { it == marker } == 2 && row.contains("")) {
                return Pair(i, row.indexOf(""))
            }
        }

        // Check for winning move in cols
        for (i in 0 until 3) {
            val col = listOf(board[0][i], board[1][i], board[2][i])
            if (col.count { it == marker } == 2 && col.contains("")) {
                return Pair(col.indexOf(""), i)
            }
        }

        // Check for winning diagonal
        val d1 = listOf(board[0][0], board[1][1], board[2][2])
        val d2 = listOf(board[0][2], board[1][1], board[2][0])

        if (d1.count { it == marker } == 2 && d1.contains("")) {
            val index = d1.indexOf("")
            return listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2))[index]
        } else if (d2.count { it == marker } == 2 && d2.contains("")) {
            val index = d2.indexOf("")
            return listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))[index]
        }

        return null
    }


    private fun updateBoard(): Pair<Array<Array<String>>?, String> {
        val resourceIds =
            listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        val board = Array(3) { Array(3) { "" } }

        for (i in resourceIds.indices) {
            val id = resourceIds[i]

            val content = getNodeTextByRes(getResId(id))

            if (content == null) return Pair(
                null,
                "Failed to retrieve object with resource id: ${getResId(id)}"
            )

            val x = i % 3
            val y = if (i <= 2) {
                0
            } else if (i <= 5) {
                1
            } else {
                2
            }

            board[y][x] = content
        }
        return Pair(board, "Successfully updated the board.")
    }


}