package es.informaticamovil.tanteo

import androidx.lifecycle.ViewModel

class ContadorVM: ViewModel() {

    var puntajeLocal: TeamPoints = TeamPoints(0,0,0);
    var puntajeVisitante: TeamPoints = TeamPoints(0,0,0);


}