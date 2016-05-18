//
// EvhLogonCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLogonCommandResponse
//
@interface EvhLogonCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* uid;

@property(nonatomic, copy) NSString* loginToken;

@property(nonatomic, copy) NSString* contentServer;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* accessPoints;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

