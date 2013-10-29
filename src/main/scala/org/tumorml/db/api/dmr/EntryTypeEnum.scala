package org.tumorml.db.api.dmr

/**
 * Created with IntelliJ IDEA.
 * User: davjoh
 * Date: 21/10/2013
 * Time: 22:17
 * To change this template use File | Settings | File Templates.
 */
object EntryTypeEnum extends Enumeration {
  type EntryTypeEnum = Value
  val InSilico, InVitro, InVivo, Clinical = Value
}
