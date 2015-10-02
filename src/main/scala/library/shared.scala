package library

object Shared {
  def resources =
    new java.net.URL(getClass.getResource("/webroot/"), ".")
}