//
// EvhListAllFamilyMembersCommandResponse.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFamilyMemberFullDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAllFamilyMembersCommandResponse
//
@interface EvhListAllFamilyMembersCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhFamilyMemberFullDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

