package me.gfumagali.btgpedidos;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@OpenAPIDefinition(info =
@Info(
        title = "Microserviço de Pedidos",
        version = "1.0",
        description = "API REST para exibição de dados de pedidos de clientes consumidos de uma fila",
        contact = @Contact(name = "Guilherme Fumagali", email = "guilhermefumarques@gmail.com")
))
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class BtgMicroServicoPedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(BtgMicroServicoPedidosApplication.class, args);
    }

}
