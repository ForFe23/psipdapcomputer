package com.dapcomputer.inventariosapi.presentacion.controladores;

import com.dapcomputer.inventariosapi.dominio.entidades.Acta;
import com.dapcomputer.inventariosapi.dominio.entidades.Equipo;
import com.dapcomputer.inventariosapi.dominio.entidades.Incidente;
import com.dapcomputer.inventariosapi.dominio.entidades.Mantenimiento;
import com.dapcomputer.inventariosapi.dominio.entidades.Usuario;
import com.dapcomputer.inventariosapi.dominio.modelo.Empresa;
import com.dapcomputer.inventariosapi.dominio.modelo.Ubicacion;
import com.dapcomputer.inventariosapi.dominio.entidades.Cliente;
import com.dapcomputer.inventariosapi.dominio.repositorios.ActaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.ClienteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.EquipoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IEmpresaRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IUbicacionRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.IncidenteRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.MantenimientoRepositorio;
import com.dapcomputer.inventariosapi.dominio.repositorios.UsuarioRepositorio;
import com.dapcomputer.inventariosapi.presentacion.dto.GlobalClienteResumenDto;
import com.dapcomputer.inventariosapi.presentacion.dto.GlobalEmpresaResumenDto;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/global")
public class GlobalControlador {
    private final ClienteRepositorio clienteRepositorio;
    private final IEmpresaRepositorio empresaRepositorio;
    private final EquipoRepositorio equipoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final IUbicacionRepositorio ubicacionRepositorio;
    private final ActaRepositorio actaRepositorio;
    private final IncidenteRepositorio incidenteRepositorio;
    private final MantenimientoRepositorio mantenimientoRepositorio;

    public GlobalControlador(
            ClienteRepositorio clienteRepositorio,
            IEmpresaRepositorio empresaRepositorio,
            EquipoRepositorio equipoRepositorio,
            UsuarioRepositorio usuarioRepositorio,
            IUbicacionRepositorio ubicacionRepositorio,
            ActaRepositorio actaRepositorio,
            IncidenteRepositorio incidenteRepositorio,
            MantenimientoRepositorio mantenimientoRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
        this.empresaRepositorio = empresaRepositorio;
        this.equipoRepositorio = equipoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.ubicacionRepositorio = ubicacionRepositorio;
        this.actaRepositorio = actaRepositorio;
        this.incidenteRepositorio = incidenteRepositorio;
        this.mantenimientoRepositorio = mantenimientoRepositorio;
    }

    @GetMapping("/resumen")
    public List<GlobalClienteResumenDto> resumen() {
        List<Cliente> clientes = clienteRepositorio.listar();
        List<Empresa> empresas = empresaRepositorio.listar();
        List<Equipo> equipos = equipoRepositorio.listar();
        List<Usuario> usuarios = usuarioRepositorio.listarTodos();
        List<Ubicacion> ubicaciones = ubicacionRepositorio.listar();
        List<Acta> actas = actaRepositorio.listar();
        List<Incidente> incidentes = incidenteRepositorio.listar();
        List<Mantenimiento> mantenimientos = mantenimientoRepositorio.listar();

        return clientes.stream()
                .map(c -> mapCliente(c, empresas, equipos, usuarios, ubicaciones, actas, incidentes, mantenimientos))
                .toList();
    }

    private GlobalClienteResumenDto mapCliente(
            Cliente cliente,
            List<Empresa> empresas,
            List<Equipo> equipos,
            List<Usuario> usuarios,
            List<Ubicacion> ubicaciones,
            List<Acta> actas,
            List<Incidente> incidentes,
            List<Mantenimiento> mantenimientos) {
        var empresasCliente = empresas.stream().filter(e -> equalsId(e.clienteId(), cliente.id())).toList();
        var detalleEmpresas = empresasCliente.stream()
                .map(emp -> mapEmpresa(emp, equipos, usuarios, ubicaciones, actas, incidentes, mantenimientos))
                .toList();

        var equiposCliente = equipos.stream().filter(e -> equalsId(e.idCliente(), cliente.id())).toList();
        var equipoIdsCliente = equiposCliente.stream().map(Equipo::id).toList();
        long equiposCount = equiposCliente.size();
        long usuariosCount = usuarios.stream().filter(u -> equalsId(u.idCliente(), cliente.id())).count();
        var empresaIds = empresasCliente.stream().map(Empresa::id).toList();
        long ubicacionesCount = ubicaciones.stream().filter(u -> empresaIds.contains(u.empresaId())).count();
        long actasCount = actas.stream().filter(a -> equalsId(a.idCliente(), cliente.id()) || empresaIds.contains(a.empresaId())).count();
        long incidentesCount = incidentes.stream()
                .filter(i -> equalsId(i.idCliente(), cliente.id()) || equipoIdsCliente.contains(i.equipoId()))
                .count();
        long mantCount = mantenimientos.stream().filter(m -> equalsId(m.idCliente(), cliente.id())).count();

        return new GlobalClienteResumenDto(
                cliente.id(),
                cliente.nombre(),
                empresasCliente.size(),
                equiposCount,
                usuariosCount,
                ubicacionesCount,
                actasCount,
                incidentesCount,
                mantCount,
                detalleEmpresas);
    }

    private GlobalEmpresaResumenDto mapEmpresa(
            Empresa empresa,
            List<Equipo> equipos,
            List<Usuario> usuarios,
            List<Ubicacion> ubicaciones,
            List<Acta> actas,
            List<Incidente> incidentes,
            List<Mantenimiento> mantenimientos) {
        var equiposEmpresa = equipos.stream().filter(e -> equalsId(e.empresaId(), empresa.id())).toList();
        var equipoIdsEmpresa = equiposEmpresa.stream().map(Equipo::id).toList();
        long equiposCount = equiposEmpresa.size();
        long usuariosCount = usuarios.stream().filter(u -> equalsId(u.empresaId(), empresa.id())).count();
        long ubicacionesCount = ubicaciones.stream().filter(u -> equalsId(u.empresaId(), empresa.id())).count();
        long actasCount = actas.stream().filter(a -> equalsId(a.empresaId(), empresa.id())).count();
        long incidentesCount = incidentes.stream().filter(i -> equipoIdsEmpresa.contains(i.equipoId())).count();
        long mantCount = mantenimientos.stream()
                .filter(m -> equiposEmpresa.stream().anyMatch(e -> equalsId(e.id(), m.equipoId())))
                .count();

        return new GlobalEmpresaResumenDto(
                empresa.id(),
                empresa.clienteId(),
                empresa.nombre(),
                equiposCount,
                usuariosCount,
                ubicacionesCount,
                actasCount,
                incidentesCount,
                mantCount);
    }

    private boolean equalsId(Long a, Long b) {
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private boolean equalsId(Integer a, Long b) {
        if (a == null || b == null) return false;
        return a.longValue() == b;
    }

    private boolean equalsId(Long a, Integer b) {
        if (a == null || b == null) return false;
        return a == b.longValue();
    }
}

