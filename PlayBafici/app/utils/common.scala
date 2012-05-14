package utils

object Common {

  def isNull[T](value: Any, default: T): T = {
    if (value == null ) {
      default
    } else {
      value.asInstanceOf[T]
    }
  }

}