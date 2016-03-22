import util.control.Breaks._

class Utility {

  var rows: Int = Play_main.rows

  var columns: Int = Play_main.columns

  def initialize(mat: Array[Array[Int]], arr: Array[Int]) {
    for (i <- 0 until rows; j <- 0 until columns) {
      mat(i)(j) = 0
    }
    for (j <- 0 until columns) {
      arr(j) = -1
    }
  }

  def printMatrix(mat: Array[Array[Int]]) {
    println("  column ids: ")
    System.out.print("  " + 0 + " ")
    for (i <- 1 until columns) {
      System.out.print("  " + i + " ")
    }
    println()
    for (i <- 0 until columns) {
      System.out.print("-----")
    }
    println()
    var i = rows - 1
    while (i >= 0) {
      System.out.print("| ")
      for (j <- 0 until columns) {
        System.out.print(mat(i)(j) + " | ")
      }
      println()
      i -= 1
    }
  }

  def hasWin(mat: Array[Array[Int]], 
      recentrowmove: Int, 
      recentcolumnmove: Int, 
      arg: Int): java.lang.Boolean = {
    checkHorizontal(mat, recentrowmove, recentcolumnmove, arg) || 
      checkVertical(mat, recentrowmove, recentcolumnmove, arg) || 
      checkDiagonalRi(mat, recentrowmove, recentcolumnmove, arg) || 
      checkDiagonalLi(mat, recentrowmove, recentcolumnmove, arg)
  }

  def checkHorizontal(mat: Array[Array[Int]], 
      row: Int, 
      column: Int, 
      arg: Int): java.lang.Boolean = {
    var tempr = row
    var tempc = column
    var count = 0
    breakable {
    while (tempc >= 0) {
      if (mat(row)(tempc) != arg) {
        break
      }
      count += 1
      tempc -= 1
    }
    }
    tempc = column + 1
    breakable {
    while (tempc < columns) {
      if (mat(row)(tempc) != arg) {
        break
      }
      count += 1
      tempc += 1
    }
    }
    if (count >= 4) {
      return true
    }
    false
  }

  def checkVertical(mat: Array[Array[Int]], 
      row: Int, 
      column: Int, 
      arg: Int): java.lang.Boolean = {
    var tempr = row
    var tempc = column
    var count = 0
    breakable {
    while (tempr >= 0) {
      if (mat(tempr)(column) != arg) {
        break
      }
      count += 1
      tempr -= 1
    }
    }
    tempr = row + 1
    breakable {
    while (tempr < rows) {
      if (mat(tempr)(column) != arg) {
        break
      }
      count += 1
      tempr += 1
    }
    }
    if (count >= 4) {
      return true
    }
    false
  }

  def checkDiagonalRi(mat: Array[Array[Int]], 
      row: Int, 
      column: Int, 
      arg: Int): java.lang.Boolean = {
    var tempr = row
    var tempc = column
    var count = 0
    breakable {
    while (tempr >= 0 && tempc >= 0) {
      if (mat(tempr)(tempc) != arg) {
        break
      }
      count += 1
      tempr -= 1
      tempc -= 1
    }
    }
    tempr = row + 1
    tempc = column + 1
    breakable {
    while (tempr < rows && tempc < columns) {
      if (mat(tempr)(tempc) != arg) {
        break
      }
      count += 1
      tempr += 1
      tempc += 1
    }
    }
    if (count >= 4) {
      return true
    }
    false
  }

  def checkDiagonalLi(mat: Array[Array[Int]], 
      row: Int, 
      column: Int, 
      arg: Int): java.lang.Boolean = {
    var tempr = row
    var tempc = column
    var count = 0
    breakable {
    while (tempr < rows && tempc >= 0) {
      if (mat(tempr)(tempc) != arg) {
        break
      }
      count += 1
      tempr += 1
      tempc -= 1
    }
    }
    tempr = row - 1
    tempc = column + 1
    breakable {
    while (tempr >= 0 && tempc < columns) {
      if (mat(tempr)(tempc) != arg) {
        break
      }
      count += 1
      tempr -= 1
      tempc += 1
    }
    }
    if (count >= 4) {
      return true
    }
    false
  }

  def getScore(mat: Array[Array[Int]], 
      row: Int, 
      column: Int, 
      arg: Int): Int = {
    var moreMoves = 0
    var machinescore = 0
    var finalScore = 0
    var tempr = row
    var tempc = column
    breakable {
    while (tempc >= 0) {
      if (mat(row)(tempc) == 0) {
        moreMoves += 1
      } else if (mat(row)(tempc) == arg) {
        machinescore += 1
      } else break
      tempc -= 1
    }
    }
    tempc = column + 1
    breakable {
    while (tempc < columns) {
      if (mat(row)(tempc) == 0) {
        moreMoves += 1
      } else if (mat(row)(tempc) == arg) {
        machinescore += 1
      } else break
      tempc += 1
    }
    }
    if (moreMoves != 0) {
      finalScore += calculateScore(machinescore, moreMoves)
    }
    machinescore = 0;
    moreMoves = 0;
    tempr = row
    tempc = column
    breakable {
    while (tempr >= 0) {
      if (mat(tempr)(column) == arg) {
        machinescore += 1
      } else break
      tempr -= 1
    }
    }
    moreMoves = rows - row - 1
    if (moreMoves != 0) {
      finalScore += calculateScore(machinescore, moreMoves)
    }
    machinescore = 0;
    moreMoves = 0;
    tempr = row
    tempc = column
    breakable {
    while (tempr >= 0 && tempc >= 0) {
      if (mat(tempr)(tempc) == 0) {
        moreMoves += 1
      } else if (mat(tempr)(tempc) == arg) {
        machinescore += 1
      } else {
        break
      }
      tempr -= 1
      tempc -= 1
    }
    }
    tempr = row + 1
    tempc = column + 1
    breakable {
    while (tempr < rows && tempc < columns) {
      if (mat(tempr)(tempc) == 0) {
        moreMoves += 1
      } else if (mat(tempr)(tempc) == arg) {
        machinescore += 1
      } else {
        break
      }
      tempr += 1
      tempc += 1
    }
    }
    if (moreMoves != 0) {
      finalScore += calculateScore(machinescore, moreMoves)
    }
    tempr = row
    tempc = column
    machinescore = 0
    moreMoves = 0
    breakable {
    while (tempr < rows && tempc >= 0) {
      if (mat(tempr)(tempc) == 0) {
        moreMoves += 1
      } else if (mat(tempr)(tempc) == arg) {
        machinescore += 1
      } else {
        break
      }
      tempr += 1
      tempc -= 1
    }
    }
    tempr = row - 1
    tempc = column + 1
    breakable {
    while (tempr >= 0 && tempc < columns) {
      if (mat(tempr)(tempc) == 0) {
        moreMoves += 1
      } else if (mat(tempr)(tempc) == arg) {
        machinescore += 1
      } else {
        break
      }
      tempr -= 1
      tempc += 1
    }
    }
    if (moreMoves != 0) {
      finalScore += calculateScore(machinescore, moreMoves)
    }
    finalScore
  }

  def calculateScore(machineScore: Int, moreMoves: Int): Int = {
    val moveScore = if ((moreMoves > 3)) 3 else moreMoves
    if (machineScore == 0) 0 else if (machineScore == 1) if ((moveScore < 3)) 0 else 1 * moveScore else if (machineScore == 2) if ((moveScore < 2)) 0 else 10 * moveScore else if (machineScore == 3) if ((moveScore < 1)) 0 else 100 * moveScore else 1000
  }
}

