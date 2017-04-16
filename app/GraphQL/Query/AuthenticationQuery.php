<?php
/**
 * Created by PhpStorm.
 * User: fatih
 * Date: 16/04/2017
 * Time: 15:17
 */

namespace App\GraphQL\Query;

use Folklore\GraphQL\Support\Query;
use GraphQL\Type\Definition\Type;
use JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;

class AuthenticationQuery extends Query
{
    public function type()
    {
        return Type::string();
    }

    public function args()
    {
        return [
            'email' => ['name' => 'email', 'type' => Type::nonNull(Type::string())],
            'password' => ['name' => 'password', 'type' => Type::nonNull(Type::string())]
        ];
    }

    public function resolve($root, $args)
    {
        $credentials = array_only($args, ['email', 'password']);
        try {
            if (!$token = JWTAuth::attempt($credentials)) {
                return '';
            }
        } catch (JWTException $e) {
            return '';
        }

        return $token;
    }
}