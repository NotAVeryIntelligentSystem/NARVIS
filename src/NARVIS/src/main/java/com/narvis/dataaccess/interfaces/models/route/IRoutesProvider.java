/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.interfaces.models.route;

import com.narvis.dataaccess.interfaces.IDataModelProvider;

/**
 *
 * @author Zack
 */
public interface IRoutesProvider extends IDataModelProvider{
    IRouteNode getRouteNode();
    void setRouteNode(IRouteNode route);
}
