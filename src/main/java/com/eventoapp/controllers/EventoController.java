package com.eventoapp.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.repository.EventoRepository;

@Controller
public class EventoController {

	@Autowired
	private EventoRepository er;
	
	@Autowired
	private ConvidadoRepository cr;

	/*
	 * Redireciona para tela do formulário de cadatro
	 */
	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
	public String from() {
		return "evento/formEvento";
	}
	
	/*
	 * Valida campos do Evento e persiste na base de dados
	 */
	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
	public String from(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {

		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/cadastrarEvento";
		}
				
		er.save(evento);
		attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
		
		return "redirect:/eventos";
	}
	
	/*
	 * Retorna todos eventos cadastrados 
	 */	
	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> eventos = er.findAll();
		mv.addObject("eventos", eventos);
		return mv;
	}
	
	/*
	 * Recebe um código de evento e retorna o Model povoado com o respectivo evento
	 */
	@RequestMapping(value="/{codigo}", method = RequestMethod.GET)
	public ModelAndView detalhesEventoGET(@PathVariable("codigo") long codigo) {		
		Evento evento = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento", evento);
		Iterable<Convidado> convidados = cr.findByEvento(evento);
		mv.addObject("convidados", convidados);		
		return mv;
		
	}
	
	/*
	 * Recebe um código de evento e um Convidado no evento
	 */
	@RequestMapping(value="/{codigo}", method = RequestMethod.POST)
	public String detalhesEventoPOST(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {		
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/{codigo}";
		}
		
		Evento evento = er.findByCodigo(codigo);
		convidado.setEvento(evento);
		cr.save(convidado);
		attributes.addFlashAttribute("mensagem", "Convidado cadastrado com sucesso!");
		return "redirect:/{codigo}";
	}	
	
	/*
	 * Removendo Evento da Lista
	 */
	@RequestMapping(value="/deletarEvento/{codigo}")
	public String deletarEvento(long codigo) {
		Evento evento = er.findByCodigo(codigo);
		er.delete(evento);
		return "redirect:/eventos";
	}
	/*
	 * Removendo convidado do Evento
	 */
	@RequestMapping(value="/deletarConvidado")
	public String deletarConvidado(String rg) {
		Convidado convidado = cr.findByRg(rg);
		cr.delete(convidado);
		Evento evento = convidado.getEvento();
		String codigoEvento = ""+ evento.getCodigo();
		return "redirect:/" + codigoEvento;
	}
	
	

}
