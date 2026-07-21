package interfaces;

import java.util.List;

import modelos.Produto;


public interface ICRUD<T,t> {
    T salvar(T obj);
    void deletar(t id);
    void alterar(T obj);
    T consultar(t id);
    List<T> consultar();
}
