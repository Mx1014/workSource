//
// EvhFindFamilyBillAndPaysByFamilyIdAndTimeCommand.h
// generated at 2016-03-31 11:07:26 
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

