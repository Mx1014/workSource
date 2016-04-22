//
// EvhDeleteQualityStandardCommand.h
// generated at 2016-04-22 13:56:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteQualityStandardCommand
//
@interface EvhDeleteQualityStandardCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* standardId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

