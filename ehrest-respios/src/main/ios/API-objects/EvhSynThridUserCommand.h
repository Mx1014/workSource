//
// EvhSynThridUserCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSynThridUserCommand
//
@interface EvhSynThridUserCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* namespaceUserToken;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* randomNum;

@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSString* sign;

@property(nonatomic, copy) NSString* key;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* deviceIdentifier;

@property(nonatomic, copy) NSString* pusherIdentify;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

