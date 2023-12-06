package moe.sekiu.seaccessbypass.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import de.robv.android.xposed.XposedHelpers

@InjectYukiHookWithXposed
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        debugLog {
            tag = "SecureElementAccessBypass"
        }
    }

    override fun onHook() = encase {
        loadApp("com.android.se") {
            "com.android.se.security.AccessControlEnforcer".toClass().method {
                name = "readSecurityProfile"
                returnType = UnitType
            }.hook {
                replaceUnit {
                    XposedHelpers.setBooleanField(instance, "mUseArf", false)
                    XposedHelpers.setBooleanField(instance, "mUseAra", false)
                    XposedHelpers.setBooleanField(instance, "mFullAccess", true)
                }
            }
        }
    }
}