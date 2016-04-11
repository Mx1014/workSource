//
// EvhGetPunchLocationCommand.h
// generated at 2016-04-07 17:57:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPunchLocationCommand
//
@interface EvhGetPunchLocationCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* companyId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

