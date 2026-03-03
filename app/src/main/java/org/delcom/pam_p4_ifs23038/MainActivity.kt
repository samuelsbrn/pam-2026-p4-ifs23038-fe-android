package org.delcom.pam_p4_ifs23038

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.delcom.pam_p4_ifs23038.ui.UIApp
import org.delcom.pam_p4_ifs23038.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorViewModel
import org.delcom.pam_p4_ifs23038.ui.viewmodels.PlantViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val plantViewModel: PlantViewModel by viewModels()
    private val motorViewModel: MotorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DelcomTheme {
                UIApp(
                    plantViewModel = plantViewModel,
                    motorViewModel = motorViewModel
                )
            }
        }
    }
}
