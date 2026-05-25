package reservation_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reservation_service.entity.Workstation;
import reservation_service.repository.WorkstationRepository;
import java.util.List;
@RestController
@RequestMapping("/api/workstations")
@RequiredArgsConstructor
public class WorkstationController {
    private final WorkstationRepository workstationRepository;

    @GetMapping("/test")
    public List<Workstation> getAllStations(){
        return workstationRepository.findAll();
    }

    @PostMapping("/create")
    public Workstation create(@RequestBody Workstation workstation){
        System.out.println("HIT");
        return workstationRepository.save(workstation);
    }


}
