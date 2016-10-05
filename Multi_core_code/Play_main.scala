import java.io._
import java.util._
import util.control.Breaks._
import Play_main._
import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import scala.language.postfixOps

object Play_main {

  var rows: Int = 7

  var columns: Int = 7
  var cores: Int = 4
  var unique: Int = 0
  var mdepth: Int = 7
  val mainObj = new Play_main()
  var system: ActorSystem = null //= ActorSystem("ConnectSystem"+unique)
  sealed trait ConnectMessage
  case object Calculate extends ConnectMessage
  case class Work(mat: Array[Array[Int]], pos: Array[Int], depth: Int, isMaximize: java.lang.Boolean, currRow: Int, 
      currCol: Int, i: Int, bscore: Double, mainObj: Play_main) extends ConnectMessage
  case class Result(tempscore: Double, index: Int) extends ConnectMessage
  case class scoreCalculation(bscore: Double, duration: Duration, obj: Play_main, depth: Int)

  def main(args: Array[String]) {
//    val mainObj = new Play_main()
    val sc = new Scanner(System.in)
    val mat = Array.ofDim[Int](rows, columns)
    val arr = Array.ofDim[Int](columns)
    var flag = 1
    system = ActorSystem("ConnectSystem"+unique)
    mainObj.util.initialize(mat, arr)
    mainObj.util.printMatrix(mat)
    var player_turn = if ((Math.random() > 0.5)) true else false
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
        if ((col >= columns || col < 0) || (arr(col) == (rows) - 1)) break//continue
        arr(col) = (arr(col) + 1)
        mat(arr(col))(col) = 1
        mainObj.util.printMatrix(mat)
        if (mainObj.util.hasWin(mat, arr(col), col, 1)) {
          println("You won the game")
          system.shutdown()
          System.exit(0)
        }
      } else {
        if (flag == 1) {
          arr((columns/2)) = (1 + arr((columns/2)))
          mat(arr((columns / 2)))((columns / 2)) = 2
          player_turn = !player_turn
          mainObj.util.printMatrix(mat)
          flag = 0
          break
        }
        val time = System.currentTimeMillis()
        mainObj.makeMove(mat, arr, mdepth, true, -1, -1,mainObj)
        println("Time taken multicore one move "+(System.currentTimeMillis() - time))

        mat(mainObj.posrow)(mainObj.poscol) = 2
        arr(mainObj.poscol) = (arr(mainObj.poscol) + 1)
        mainObj.util.printMatrix(mat)
        if (mainObj.util.hasWin(mat, mainObj.posrow, mainObj.poscol, 2)) {
          println("I won the Game! What say?")
          system.shutdown()
          System.exit(0)
        }
        println("PLAYED AT " + mainObj.poscol)
      }
      player_turn = !player_turn
    }
    }
  }
}

class Worker extends Actor {

    var util: Utility = new Utility()
 
    def calculateoneMove(mat: Array[Array[Int]], pos: Array[Int], depth: Int, isMaximize: java.lang.Boolean, currRow: Int, 
      currCol: Int, i: Int, bscore: Double, obj: Play_main): Double = {

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
        } else {
          if (tempscore <= score) {
            score = tempscore
          }
        }

      }
      if (depth > 1) {
        val tempscore = calculateoneMove(mat, pos, depth - 1, !isMaximize, pos(i), i, i, 0.0, new Play_main())
        if (isMaximize) {
          if (score <= tempscore) {
            score = tempscore
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
 
    def receive = {
      case Work(mat, pos, depth, isMaximize, currRow, currCol, i, bscore, obj) ⇒
        sender ! Result(calculateoneMove(mat, pos, depth, isMaximize, currRow, currCol, i, bscore, obj), i)
    }
  }



  class Master(nrOfWorkers: Int, nrOfMessages: Int, mat: Array[Array[Int]], pos: Array[Int], depth: Int, isMaximize: java.lang.Boolean, currRow: Int, currCol: Int, i: Int, bscore: Double, obj: Play_main, listener: ActorRef)
    extends Actor {
 
    var score: Double = bscore
    var nrOfResults: Int = _
    var workercount: Int = 0
    val start: Long = System.currentTimeMillis
    var tempsender: ActorRef = _
    val workerRouter = context.actorOf(
      Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")

     def duplicatemat(mat: Array[Array[Int]]): Array[Array[Int]] = {
       val dupmat = Array.ofDim[Int](rows, columns)

       for (i <- 0 until rows; j <- 0 until columns) {
        dupmat(i)(j) = mat(i)(j)
       }
       dupmat
     }


    def duplicatearr( arr: Array[Int]): Array[Int] = {

      val duparr = Array.ofDim[Int](columns)
      
      for (j <- 0 until columns) {
        duparr(j) = arr(j)
      }
      duparr
    } 
 
    def receive = {
      case Calculate ⇒
        tempsender = sender
        workercount = 0
        for (i ← 0 until nrOfMessages)
        {
	    var tempmat = duplicatemat(mat)
            var temppos = duplicatearr(pos)
            temppos(i) += 1
	    breakable{
            if (temppos(i) >= rows) {
              temppos(i) -= 1
              break
            }
	    workercount +=1
            tempmat(temppos(i))(i) = if (isMaximize) 2 else 1
            workerRouter ! Work(tempmat, temppos, depth-1, !isMaximize, temppos(i), i, i, bscore, new Play_main())//mainObj)
           }
        }
      case Result(tempscore, index) ⇒
        if (isMaximize) {
          if (score <= tempscore) {
            score = tempscore
          }
          if (depth == mdepth) {
            if (score <= tempscore) {
              obj.posrow = (pos(index)+1)
              obj.poscol = index
            }
          //  println("Score for location " + index + " = " + tempscore + " final : " + 
          //    score)  //tracing the score
          }
        } else {
          if (tempscore <= score) {
            score = tempscore
          }
        }
        nrOfResults += 1
        if (nrOfResults == workercount) {
	  tempsender ! score
          context.stop(self)
        }
    }
 
  }

class Play_main {

  var posrow: Int = -1
  var poscol: Int = -1
  var util: Utility = new Utility()
  var mscore = 0.0

def calculate(nrOfWorkers: Int, nrOfMessages: Int, mat: Array[Array[Int]], pos: Array[Int], depth: Int, isMaximize: java.lang.Boolean, currRow: Int, currCol: Int, i: Int, bscore: Double, mainObj: Play_main) {
  
    val listener = system.actorOf(Props(new ScoreListener()), name = "listener"+Math.random())
 
    val master = system.actorOf(Props(new Master(
      nrOfWorkers, nrOfMessages, mat, pos, depth, isMaximize, currRow, currCol, i, bscore, mainObj, listener)),
      name = "master"+Math.random())
      unique = unique + 1
 

    implicit val timeout = Timeout(30 seconds)
    val future = master ? Calculate
    mscore = Await.result(future, timeout.duration).asInstanceOf[Double] 
  }

  def makeMove(mat: Array[Array[Int]], 
      pos: Array[Int], 
      depth: Int, 
      isMaximize: java.lang.Boolean, 
      currRow: Int, 
      currCol: Int, obj: Play_main): Double = {
    if (currRow >= 0 && currRow < rows && currCol >= 0 && currCol < columns) {
      if (util.hasWin(mat, currRow, currCol, 2)) {
        return java.lang.Integer.MAX_VALUE / (mdepth - depth + 1)
      } else if (util.hasWin(mat, currRow, currCol, 1)) {
        return java.lang.Integer.MIN_VALUE / (mdepth - depth + 1)
      }
    }
    var score = 0.0
    score = if (isMaximize) java.lang.Integer.MIN_VALUE else java.lang.Integer.MAX_VALUE

    calculate(cores, columns, mat, pos, depth, isMaximize, currRow, currCol, 0, score, obj)
    score = mscore
    score
  }
}

