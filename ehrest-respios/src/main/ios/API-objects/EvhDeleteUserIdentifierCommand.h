//
// EvhDeleteUserIdentifierCommand.h
// generated at 2016-04-19 13:40:00 
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

