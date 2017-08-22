package com.stripe.dagon

/**
 * This is a useful cache for memoizing heterogenously types functions
 */
sealed class HCache[K[_], V[_]] private (init: HMap[K, V]) {
  private var hmap: HMap[K, V] = init

  /**
   * Get an immutable snapshot of the current state
   */
  def snapshot: HMap[K, V] = hmap

  /**
   * Get a mutable copy of the current state
   */
  def duplicate: HCache[K, V] = new HCache(hmap)

  def getOrElseUpdate[T](k: K[T], v: => V[T]): V[T] =
    hmap.get(k) match {
      case Some(exists) => exists
      case None =>
        val res = v
        hmap = hmap + (k -> res)
        res
    }
}

object HCache {
  def empty[K[_], V[_]]: HCache[K, V] = new HCache(HMap.empty)
}
