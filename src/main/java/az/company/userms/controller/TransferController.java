package az.company.userms.controller;

import az.company.userms.exception.NotFoundException;
import az.company.userms.model.TransferDto;
import az.company.userms.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public void transfer(@RequestBody TransferDto request) throws NotFoundException {
        transferService.transfer(request);
    }
}
