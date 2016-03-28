//
// EvhGetFamilyStatisticCommand.h
// generated at 2016-03-25 19:05:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetFamilyStatisticCommand
//
@interface EvhGetFamilyStatisticCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* familyId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

