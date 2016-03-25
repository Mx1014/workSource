//
// EvhGetFamilyStatisticCommand.h
// generated at 2016-03-25 09:26:41 
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

