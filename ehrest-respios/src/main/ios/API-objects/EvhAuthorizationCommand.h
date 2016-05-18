//
// EvhAuthorizationCommand.h
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

