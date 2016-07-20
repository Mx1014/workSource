//
// EvhFindFamilyBillAndPaysByFamilyIdAndTimeCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindFamilyBillAndPaysByFamilyIdAndTimeCommand
//
@interface EvhFindFamilyBillAndPaysByFamilyIdAndTimeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* billDate;

@property(nonatomic, copy) NSNumber* familyId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

