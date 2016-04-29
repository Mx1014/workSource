//
// EvhAuthorizationCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAuthorizationCommand
//
@interface EvhAuthorizationCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* response_type;

@property(nonatomic, copy) NSString* client_id;

@property(nonatomic, copy) NSString* redirect_uri;

@property(nonatomic, copy) NSString* scope;

@property(nonatomic, copy) NSString* state;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

