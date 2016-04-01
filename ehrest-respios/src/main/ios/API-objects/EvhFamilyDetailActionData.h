//
// EvhFamilyDetailActionData.h
// generated at 2016-03-31 20:15:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyDetailActionData
//
@interface EvhFamilyDetailActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* familyId;

@property(nonatomic, copy) NSNumber* inviterId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

