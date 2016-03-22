import java.io._
import java.util._
import util.control.Breaks._
import Play_main._

object Play_main {

  var rows: Int = 7

  var columns: Int = 7

  var mdepth: Int = 8

  def main(args: Array[String]) {
    val mainObj = new Play_main()
    val sc = new Scanner(System.in)
    val mat = Array.ofDim[Int](rows, columns)
    val arr = Array.ofDim[Int](columns)
    var flag = 1
    mainObj.util.initialize(mat, arr)
    mainObj.util.printMatrix(mat)
    var player_turn = if ((Math.random() > 0.5)) true else false
    player_turn = false
    if (player_turn) {
      println("It's your turn")
    } else {
      println("It's my turn")
    }
    while (true) {
    breakable {
      if (player_turn) {
        println("Enter a valid column number between 0-6")
        val col = sc.nextInt()
        if ((col >= columns || col < 0) || (arr(col) == (rows) - 1)) break
        arr(col) += 1
        mat(arr(col))(col) = 1
        mainObj.util.printMatrix(mat)
        if (mainObj.util.hasWin(mat, arr(col), col, 1)) {
          println("You won the game")
          System.exit(0)
        }
      } else {
        if (flag == 1) {
          arr((columns/2)) += 1
          mat(arr((columns / 2)))((columns / 2)) = 2
          player_turn = !player_turn
          mainObj.util.printMatrix(mat)
          flag = 0
          break
        }
        val time = System.currentTimeMillis()
        mainObj.makeMove(mat, arr, mdepth, true, -1, -1)
        println("Time taken sequential one move "+(System.currentTimeMillis() - time))

        mat(mainObj.posrow)(mainObj.poscol) = 2
        arr(mainObj.poscol) += 1
        mainObj.util.printMatrix(mat)
        if (mainObj.util.hasWin(mat, mainObj.posrow, mainObj.poscol, 2)) {
          println("I won the Game! What say?")
          System.exit(0)
        }
        println("PLAYED AT " + mainObj.poscol)
      }
      player_turn = !player_turn
    }
    }
  }
}

class Play_main {

  var posrow: Int = -1

  var poscol: Int = -1

  var util: Utility = new Utility()

  def makeMove(mat: Array[Array[Int]], 
      pos: Array[Int], 
      depth: Int, 
      isMaximize: java.lang.Boolean, 
      currRow: Int, 
      currCol: Int): Double = {
    if (currRow >= 0 && currRow < rows && currCol >= 0 && currCol < columns) {
      if (util.hasWin(mat, currRow, currCol, 2)) {
        return java.lang.Integer.MAX_VALUE / (mdepth - depth + 1)
      } else if (util.hasWin(mat, currRow, currCol, 1)) {
        return java.lang.Integer.MIN_VALUE / (mdepth - depth + 1)
      }
    }
    var score = 0.0
    score = if (isMaximize) java.lang.Integer.MIN_VALUE else java.lang.Integer.MAX_VALUE

    for (i <- 0 until columns) {
     breakable {
      pos(i) += 1
      if (pos(i) >= rows) {
        pos(i) -= 1
        break
      }
      mat(pos(i))(i) = if (isMaximize) 2 else 1
      if (depth == 1) {
        val tempscore = util.getScore(mat, pos(i), i, 2)

        if (isMaximize) {
          if (score <= tempscore) {
            score = tempscore
          }
          if (depth == mdepth) {
            if (score <= tempscore) {
              posrow = pos(i)
              poscol = i
            }
            //println("Score for location " + i + " = " + tempscore + " final : " +
            //  score) //tracing score
          }
        } else {
          if (tempscore <= score) {
            score = tempscore
          }
        }

      }
      if (depth > 1) {
        val tempscore = makeMove(mat, pos, depth - 1, !isMaximize, pos(i), i)
        if (isMaximize) {
          if (score <= tempscore) {
            score = tempscore
          }
          if (depth == mdepth) {
            if (score <= tempscore) {
              posrow = pos(i)
              poscol = i
            }
           // println("Score for location " + i + " = " + tempscore + " final : " + 
           //   score)  //tracing score
          }
        } else {
          if (tempscore <= score) {
            score = tempscore
          }
        }
      }
      mat(pos(i))(i) = 0
      pos(i) -= 1
     }
    }
    score
  }
}

