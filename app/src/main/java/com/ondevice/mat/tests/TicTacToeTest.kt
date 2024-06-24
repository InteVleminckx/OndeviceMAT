package com.ondevice.mat.tests

import android.util.Log
import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import com.ondevice.mat.automation.Interactor
import com.ondevice.mat.automation.NodeInfo
import com.ondevice.mat.automation.Parser
import kotlin.math.E
import kotlin.system.measureTimeMillis

class TicTacToeTest : Test() {

    override val packageName = "ug.lecode.tictactoe"

    init {
        appName = "TicTicToe"
    }

    override suspend fun runTests() {
        super.runTests()

        executeTest(::automaticTicTacToe, 1000, "Tic Tac Toe")
    }

    private fun updateBoard(): Array<Array<String>>? {
        val resourceIds = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        val board = Array(3) { Array(3) { "" } }

        for (i in resourceIds.indices) {
            val id = resourceIds[i]
            val square = engine?.retrieveNode(id, Engine.searchTypes.RESOURCE_ID) ?: return null

            val x = i % 3
            val y = if (i <= 2) {
                0
            } else if (i <= 5) {
                1
            } else {
                2
            }
            board[y][x] = square.nodeText()
        }
        return board
    }

    private fun getIdResource(row: Int, col: Int): String {

        val ids = arrayOf(
            arrayOf("one", "two", "three"),
            arrayOf("four", "five", "six"),
            arrayOf("seven", "eight", "nine")
        )
        return ids[row][col]
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

    private suspend fun setMove(position: Pair<Int, Int>): Pair<Boolean, String> {

        val resourceId: String = getIdResource(position.first, position.second)

        val square: NodeInfo = engine?.retrieveNode(resourceId, Engine.searchTypes.RESOURCE_ID) ?: return Pair(false, "Failed to retrieve node with resource id: $resourceId")

        val clickSucceed = engine?.click(square, Pair(resourceId, Engine.searchTypes.RESOURCE_ID)) ?: return Pair(false, "Failed to retrieve node with resource id: $resourceId")

        if (!clickSucceed) {
            return Pair(false, "Failed to click the object with resource id: $resourceId")
        }

        return Pair(true, "Successfully clicked node: $resourceId")

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

    private suspend fun automaticTicTacToe(numberOfIterations: Int): Pair<Boolean, String> {

        val human = "X"
        val phone = "0"

        for (i in 0 until numberOfIterations) {
            val board = updateBoard() ?: return Pair(false, "Failed to update the tic tac toe bord.")


            if (i % 99 == 0 && i != 0) {
                Log.v(OUTPUT_TAG, "--- Completed ${i + 1} iterations ---")
            }

            // Check if there is a winning move
            val winningMove = getBestMove(human, board)
            if (winningMove != null) {
                board[winningMove.first][winningMove.second] = human
                val moveValue = setMove(winningMove)
                if (!moveValue.first) {
                    return moveValue
                }
                logToFile(moveValue.second)
                continue
            }

            // Check if the phone has a winning move, and block him
            val blockingMove = getBestMove(phone, board)
            if (blockingMove != null) {
                board[blockingMove.first][blockingMove.second] = human
                val moveValue = setMove(blockingMove)
                if (!moveValue.first) {
                    return moveValue
                }
                logToFile(moveValue.second)
                continue
            }

            // No winning or blocking move, place in a random corner or center
            val emptyFields = getEmptyFields(board)
            val preferredPlaces = listOf(Pair(0, 0), Pair(0, 2), Pair(1, 1), Pair(2, 0), Pair(2, 2))
            val preferredMove = preferredPlaces.firstOrNull { it in emptyFields }

            val move = preferredMove ?: emptyFields.random()
            board[move.first][move.second] = human
            val moveValue = setMove(move)
            if (!moveValue.first) {
                return moveValue
            }
            logToFile(moveValue.second)

        }

        return Pair(true, "Successfully executed the tic tac toe test.")
    }
}