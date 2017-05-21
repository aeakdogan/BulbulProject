<?php

namespace App\Http\Controllers;

use Response;
use Storage;

class ImageController extends Controller
{

    public function show($name)
    {
        try {
           $path = '/icons/'.$name;

            $file = Storage::get($path);
            $type = Storage::mimeType($path);

            $response = Response::make($file, 200);
            $response->header("Content-Type", $type);

            return $response;
        } catch (\Exception $e) {
            return abort(404);
        }
    }

}
