package drewcarlson.walletkit

import walletkit.core.*
import kotlin.native.concurrent.*

public actual class NetworkFee(
        core: WKNetworkFee,
        take: Boolean
) : Closeable {

    internal actual constructor(
            timeIntervalInMilliseconds: ULong,
            pricePerCostFactor: Amount
    ) : this(
            checkNotNull(wkNetworkFeeCreate(
                    timeIntervalInMilliseconds,
                    pricePerCostFactor.core,
                    pricePerCostFactor.unit.core
            )),
            false
    )

    internal val core: WKNetworkFee =
            if (take) checkNotNull(wkNetworkFeeTake(core))
            else core

    public actual val timeIntervalInMilliseconds: ULong =
            wkNetworkFeeGetConfirmationTimeInMilliseconds(core)
    internal actual val pricePerCostFactor: Amount =
            Amount(checkNotNull(wkNetworkFeeGetPricePerCostFactor(core)), false)

    init {
        freeze()
    }

    actual override fun hashCode(): Int = core.hashCode()
    actual override fun equals(other: Any?): Boolean =
            other is NetworkFee && WK_TRUE == wkNetworkFeeEqual(core, other.core)

    actual override fun close() {
        wkNetworkFeeGive(core)
    }
}
