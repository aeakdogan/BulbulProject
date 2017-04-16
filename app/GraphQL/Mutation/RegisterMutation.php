<?php
/**
 * Created by PhpStorm.
 * User: fatih
 * Date: 16/04/2017
 * Time: 15:53
 */

namespace App\GraphQL\Mutation;


use App\BulbulUser;
use Folklore\GraphQL\Support\Mutation;
use GraphQL\Type\Definition\Type;
use JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;

class RegisterMutation extends Mutation
{
    protected $attributes = [
        'name' => 'register'
    ];

    public function type()
    {
        return Type::string();
    }

    public function args()
    {
        return [
            'username' => ['name' => 'username', 'type' => Type::nonNull(Type::string())],
            'password' => ['name' => 'password', 'type' => Type::nonNull(Type::string())],
            'email' => ['name' => 'email', 'type' => Type::nonNull(Type::string())],
        ];
    }

    public function rules()
    {
        return [
            'username' => 'required|unique:BulbulUser|min:1|max:255',
            'email' => 'required|email|unique:BulbulUser',
            'password' => 'required'
        ];
    }

    public function resolve($root, $args)
    {
        BulbulUser::create([
            'username' => $args['username'],
            'email' => $args['email'],
            'password' => bcrypt($args['password']),
        ]);

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