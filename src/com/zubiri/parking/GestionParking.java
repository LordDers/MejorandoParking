package com.zubiri.parking;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GestionParking
 */
public class GestionParking extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GestionParking() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType( "text/html; charset=iso-8859-1" );
		if (ParkingVehiculos.getVehiculos().size()==0){
			// Lectura del archivo
			ParkingVehiculos.leerVehiculos();
			//ParkingVehiculos.anyadirVehiculo(new Coche(4,true,"marca_prueba","0000AAA",true,50));
			//ParkingVehiculos.anyadirVehiculo(new Coche(4,true,"ferrari","0001ABA",true,100));
			//ParkingVehiculos.anyadirVehiculo(new Coche(4,true,"fiat","0002ACA",true,10));
		}
		
		String gestion = request.getParameter("gestion");
		System.out.println(gestion);
		
		if (gestion.equals("mostrar_vehiculos")){
			System.out.println("Empieza mostrando");
			response(response,ParkingVehiculos.getVehiculos());
		} else if (gestion.equals("buscar_matricula")) {
			System.out.println("Empieza buscando");
			String matricula=request.getParameter("matricula");
			Vehiculo encontrado = new Coche();
			try {
				encontrado = ParkingVehiculos.buscarVehiculo(matricula);
				response(response, encontrado);
			} catch (ArrayIndexOutOfBoundsException e) {
				response(response, "No se encontró el vehículo");
			}
		} else if (gestion.equals("anyadir_vehiculo")) {
			System.out.println("Empieza añadiendo");
			int n_ruedas = Integer.parseInt(request.getParameter("numruedas"));
			boolean motor = Boolean.parseBoolean(request.getParameter("motor"));
			String marca = request.getParameter("marca");
			String matricula = request.getParameter("matricula");
			boolean automatico = Boolean.parseBoolean(request.getParameter("automatico"));
			int consumo = Integer.parseInt(request.getParameter("consumo"));	
			System.out.println("Nuevo coche");
			try {
				if (ParkingVehiculos.buscarVehiculo(matricula) != null) {
					response(response, "La matricula introducida ya existe");
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				Coche nuevo = new Coche(n_ruedas,motor,marca,matricula,automatico,consumo);
				ParkingVehiculos.anyadirVehiculosFichero(nuevo);
				ParkingVehiculos.anyadirCoche(nuevo);
				response(response, "Vehículo añadido");
			}
		} else if (gestion.equals("borrar_vehiculo")) {
			System.out.println("Borrando");
			String matricula = request.getParameter("matricula");
			try {
				if (ParkingVehiculos.buscarVehiculo(matricula) != null) {
					ParkingVehiculos.borrarVehiculosFichero(matricula);
					ParkingVehiculos.borrarVehiculo(matricula);
					response(response, "Se ha borrado el vehículo");
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				response(response, "El vehículo no existe");
			}
			/*ParkingVehiculos.borrarVehiculosFichero(matricula);
			ParkingVehiculos.borrarVehiculo(matricula);
			response(response, "Se ha borrado el vehículo");*/
		} else if (gestion.equals("modificar_vehiculo")) {
			System.out.println("Empieza modificando");
			int n_ruedas = Integer.parseInt(request.getParameter("numruedas"));
			boolean motor = Boolean.parseBoolean(request.getParameter("motor"));
			String marca = request.getParameter("marca");
			String matriculanueva = request.getParameter("matriculanueva");
			String matriculavieja = request.getParameter("matriculavieja");
			boolean automatico = Boolean.parseBoolean(request.getParameter("automatico"));
			int consumo = Integer.parseInt(request.getParameter("consumo"));
			
			try {
				if (ParkingVehiculos.buscarVehiculo(matriculavieja) != null) {
					try {
						if (ParkingVehiculos.buscarVehiculo(matriculanueva) != null) {
							response(response, "La matricula introducida ya existe");
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						ParkingVehiculos.modificarVehiculosFicheroServlet(matriculavieja, matriculanueva, marca, n_ruedas, motor, automatico, consumo);
						ParkingVehiculos.modificarVehiculoServlet(matriculavieja, matriculanueva, marca, n_ruedas, motor, automatico, consumo);
						System.out.println("Vehículo modificado");
						response(response, "Vehículo modificado");
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				response(response, "No se encontró el vehículo");
			}
		}
		System.out.println("Fin");
	}

	
	// Mostrar vehículos
		private void response(HttpServletResponse response, ArrayList<Vehiculo> vehiculos) throws IOException {
			response.setContentType( "text/html; charset=iso-8859-1" );
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<p>-------------------------------</p>");
			for (int i=0; i<vehiculos.size(); i++) {				
				out.println("<b> Matrícula: </b>" + vehiculos.get(i).getMatricula() + " | ");
				out.print("<b> Marca: </b>" + vehiculos.get(i).getMarca() + "");
				out.println("<p>-------------------------------</p>");
			}
			out.println("<a href='index.html'> Volver </a>");
			out.println("</body>");
			out.println("</html>");
		}
		
		// Añadir, borrar y modificar vehículo
		private void response(HttpServletResponse response,String msg) throws IOException {
			response.setContentType( "text/html; charset=iso-8859-1" );
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");				
			out.println("<p>" + msg + "</p>");
			out.println("<a href='index.html'> Volver </a>");
			out.println("</body>");
			out.println("</html>");
		}
		
		// Buscar vehículo
		private void response(HttpServletResponse response, Vehiculo coche) throws IOException {
			response.setContentType( "text/html; charset=iso-8859-1" );	
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<p>" + coche.getMarca() + "</p>");
			out.println("<p>" + coche.getMatricula() + "</p>");
			out.println("<a href='index.html'> Volver </a>");
			out.println("</body>");
			out.println("</html>");
		}
}
