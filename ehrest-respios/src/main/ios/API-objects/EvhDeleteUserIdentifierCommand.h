//
// EvhDeleteUserIdentifierCommand.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteUserIdentifierCommand
//
@interface EvhDeleteUserIdentifierCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userIdentifierId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

