//
// EvhDeleteUserIdentifierCommand.h
// generated at 2016-03-30 10:13:08 
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

