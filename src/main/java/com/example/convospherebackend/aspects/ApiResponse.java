package com.example.convospherebackend.aspects;


import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ApiResponse<T> {

    private T data;
    private ApiError apiError;

    public ApiResponse(T data){
        this.data = data;
    }

    public ApiResponse(ApiError error){
        this.apiError = error;
    }

}
