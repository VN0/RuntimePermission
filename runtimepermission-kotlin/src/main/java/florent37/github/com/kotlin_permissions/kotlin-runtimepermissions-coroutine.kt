package florent37.github.com.kotlin_permissions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.sensorberg.permissionbitte.RuntimePermission
import com.sensorberg.permissionbitte.PermissionResult
import kotlin.coroutines.experimental.suspendCoroutine

suspend fun FragmentActivity.askPermission(vararg permissions: String): PermissionResult = suspendCoroutine { continuation ->
    RuntimePermission.askPermission(this)
            .request(permissions.toList())
            .onResponse { result ->
                when {
                    result.isAccepted -> continuation.resume(result)
                    else -> continuation.resumeWithException(PermissionException(result))
                }
            }
            .ask()
}

suspend fun Fragment.askPermission(vararg permissions: String): PermissionResult = suspendCoroutine { continuation ->
    when (activity) {
        null -> continuation.resumeWithException(NoActivityException())
        else -> RuntimePermission.askPermission(this)
                .request(permissions.toList())
                .onResponse { result ->
                    when {
                        result.isAccepted -> continuation.resume(result)
                        else -> continuation.resumeWithException(PermissionException(result))
                    }
                }
                .ask()
    }
}

