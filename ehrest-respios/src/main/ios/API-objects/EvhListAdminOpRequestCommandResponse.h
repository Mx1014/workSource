//
// EvhListAdminOpRequestCommandResponse.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhGroupOpRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAdminOpRequestCommandResponse
//
@interface EvhListAdminOpRequestCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhGroupOpRequestDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

