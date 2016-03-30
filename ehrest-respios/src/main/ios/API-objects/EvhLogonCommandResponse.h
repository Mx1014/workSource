//
// EvhLogonCommandResponse.h
// generated at 2016-03-30 10:13:09 
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

