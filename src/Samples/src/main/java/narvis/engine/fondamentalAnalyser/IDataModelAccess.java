/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.engine.fondamentalAnalyser;

/**
 *
 * @author Zack
 */
public interface IDataModelAccess<T> {
    T getModel();
    void persist();
}
